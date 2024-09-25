package com.document.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.Company;
import com.document.entity.InvDetail;
import com.document.entity.InvHead;
import com.document.entity.RunningParam;
import com.document.entity.views.vinvhead;
import com.document.model.InvHeadReport;
import com.document.model.InvHeadRequest;
import com.document.model.InvHeadResponse;
import com.document.repository.CompanyRepository;
import com.document.repository.InvDetailRepository;
import com.document.repository.InvHeadRepository;
import com.document.repository.RunningParamRepository;
import com.document.repository.VinvheadRepository;
import com.document.utility.AuthUtil;
import com.document.utility.NumberUtils;
import com.document.utility.ReportUtils;

@Slf4j
@Service
public class InvHeadService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    InvHeadRepository invHeadRepository;

    @Autowired
    CompanyRepository companyRepository;
    
    @Autowired
    RunningParamRepository runningParamRepository;
    
    @Autowired
    InvDetailRepository invDetailRepository;
    
    @Autowired
    VinvheadRepository vinvheadRepository;
    
	@Autowired
	ReportService reportService;
    
    public Page<InvHead> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,String inv_No, String inv_DateFr, String inv_DateTo,String project,String sender,String receiver_Name,String status){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return invHeadRepository.findAllByCriteria( inv_No, inv_DateFr,inv_DateTo,project,sender,receiver_Name,status, pageable);
    }
    
    @Transactional
    public List<InvHeadResponse> addFieldDesc(List<InvHead> contents) {
        List<InvHeadResponse> response = new ArrayList<>();
        for(InvHead content:contents){
        	InvHeadResponse invHeadResponse = InvHeadResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .inv_No(content.getInv_No())
                    .inv_Date(content.getInv_Date())
                    .total(content.getTotal())
                    .inv_amt(content.getInv_amt())
                    .vat(content.getVat())
                    .after_vat(content.getAfter_vat())
                    .withholdingTax(content.getOther_tax())
                    .summary(content.getSummary())
                    .project(content.getProject())
                    .receiver_Name(content.getReceiver_Name())
                    .sender(content.getSender())
                    .status(content.getStatus())
                    .prs_Name(content.getPrs_Name())
                    .rec_Name(content.getRec_Name())
                    .build();
        	
            if (content.getCompany() != null){
            	Company company = companyRepository.findById(content.getCompany().getId());
            	invHeadResponse.setCompanyId(company.getId());

            }
        	
            response.add(invHeadResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createInvHead(InvHeadRequest payload) {
    	InvHead invHead= new InvHead();
    	invHead.setCreatedBy(authUtil.getUsernameFromSession());
    	invHead.setCreatedDate(new Date());
    	invHead.setInv_Date(payload.getInv_Date());
    	invHead.setTotal(payload.getTotal());
    	Double INVAMT = (payload.getInv_amt());
    	invHead.setInv_amt(INVAMT);
    	Double VAT = (INVAMT*7/100);
    	invHead.setVat(VAT);
    	invHead.setAfter_vat(INVAMT+VAT);
    	Double WDT = (INVAMT*3/100);
    	invHead.setOther_tax(WDT);
    	Double SUM = (INVAMT+VAT-WDT);
    	invHead.setSummary(SUM);
    	invHead.setCompany(payload.getCompany());
    	invHead.setProject(payload.getProject());
    	invHead.setReceiver_Name(payload.getReceiver_Name());
    	invHead.setSender(payload.getSender());
    	invHead.setStatus(payload.getStatus());
    	invHead.setPrs_Name(payload.getPrs_Name());
    	invHead.setRec_Name(payload.getRec_Name());
        
        RunningParam runningParam = runningParamRepository.findByParamCode(payload.getParam());
        if(runningParam == null) { 	
            runningParam = new RunningParam();
            runningParam.setParamCode(payload.getParam());
            runningParam.setParamValue(0);
            runningParamRepository.save(runningParam);
        }
        runningParam.setParamValue(runningParam.getParamValue() + 1);
        runningParamRepository.save(runningParam);
        

        int year = payload.getInv_Date().getYear() % 100;
        invHead.setInv_No(payload.getParam()+(year+43)+String.format("%04d", runningParam.getParamValue()));
        invHeadRepository.save(invHead);
        List<InvDetail> detail = new ArrayList<>();
        
        
        for(InvDetail n : payload.getInvDetail()) {
        	InvDetail old = invDetailRepository.findById(n.getId());
        	old.setCreatedBy(authUtil.getUsernameFromSession());
        	old.setCreatedDate(new Date());
        	old.setName(n.getName());
        	old.setNum(n.getNum());
        	old.setInv_amt(n.getInv_amt());
        	old.setDiscount(n.getDiscount());
        	old.setPay(n.getPay());
        	old.setInvHead(invHead);
        	old.setQuotation(old.getQuotation());
        	old.setReceipt(old.getReceipt());
        	
        	detail.add(old);
        	
        }
    	invDetailRepository.saveAll(detail);
        
        
     }
    
	public void update(Long id, InvHeadRequest req) {
		InvHead old = invHeadRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(old.getCreatedDate());
		old.setInv_No(old.getInv_No());
		if(req.getInv_Date() == null) {
			old.setInv_Date(old.getInv_Date());
		}else {
			old.setInv_Date(req.getInv_Date());
		}
		if(req.getTotal() == null) {
			old.setTotal(old.getTotal());
		}else {
			old.setTotal(req.getTotal());
		}
		Double INVAMT = null;
		if(req.getInv_amt() == null) {
			INVAMT = (old.getInv_amt());
		}else {
			INVAMT = (req.getInv_amt());
		}
		old.setInv_amt(INVAMT);
		Double VAT = (INVAMT*7/100);
		old.setVat(VAT);
		old.setAfter_vat(INVAMT+VAT);
		Double WDT = (INVAMT*3/100);
		old.setOther_tax(WDT);
    	old.setSummary(INVAMT+VAT-WDT);
		if(req.getCompany() == null) {
			old.setCompany(old.getCompany());
		}else {
	    	old.setCompany(req.getCompany());
		}
		if(req.getProject() == null) {
			old.setProject(old.getProject());
		}else {
	    	old.setProject(req.getProject());
		}
		if(req.getReceiver_Name() == null) {
	    	old.setReceiver_Name(old.getReceiver_Name());
		}else {
	    	old.setReceiver_Name(req.getReceiver_Name());
		}
		if(req.getSender() == null) {
			old.setSender(old.getSender());
		}else {
	    	old.setSender(req.getSender());
		}
		if(req.getStatus() == null) {
			old.setStatus(old.getStatus());
		}else {
	    	old.setStatus(req.getStatus());
		}
		if(req.getPrs_Name() == null) {
	    	old.setPrs_Name(old.getPrs_Name());
		}else {
	    	old.setPrs_Name(req.getPrs_Name());
		}
		if(req.getRec_Name() == null) {
			old.setRec_Name(old.getRec_Name());
		}else {
	    	old.setRec_Name(req.getRec_Name());
		}

    	invHeadRepository.save(old);
	}
	
    public String genInvHead(InvHeadReport request) {

    	
        List<vinvhead> Head = vinvheadRepository.findDataReport(request.getInvHead());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vinvhead old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vinvhead();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vinvhead newInvHead = new vinvhead();

        	newInvHead.setProject(old.getProject());
            newInvHead.setSender(old.getSender());
            newInvHead.setReceiver_Name(old.getReceiver_Name());
            newInvHead.setStatus(old.getStatus());
            newInvHead.setInv_No(old.getInv_No());
            newInvHead.setInv_Date(old.getInv_Date());
            newInvHead.setTotal(old.getTotal());
            newInvHead.setInvh_amt(old.getInvh_amt());
            newInvHead.setVat(old.getVat());
            newInvHead.setAfter_vat(old.getAfter_vat());
            newInvHead.setOther_tax(old.getOther_tax());
            newInvHead.setSummary(old.getSummary());
            Head.add(newInvHead);
        }
        
        if (!Head.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            List<Company> companys = companyRepository.findById2("Company");
            log.info("LOGGG"+companys);
            Company company = companys.get(0);
            log.info("TESTTTTTTT"+company);
            params.put("CNAME_TH", company.getName_th());
            params.put("CNAME_EN", company.getName_en());
            params.put("CADDRESS_TH1", company.getAddress_th1());
            params.put("CADDRESS_TH2", company.getAddress_th2());
            params.put("CADDRESS_EN1", company.getAddress_en1());
            params.put("CADDRESS_EN2", company.getAddress_en2());
            params.put("CTEL", company.getTel());
            params.put("CEMAIL", company.getEmail());
            params.put("CTAXID", company.getTaxId());
            params.put("URL", ReportUtils.getLogo());
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getSummary().doubleValue()));
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("invhreport.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
    public String genInvHeadCopy(InvHeadReport request) {

    	
        List<vinvhead> Head = vinvheadRepository.findDataReport(request.getInvHead());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vinvhead old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vinvhead();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vinvhead newInvHead = new vinvhead();

        	newInvHead.setProject(old.getProject());
            newInvHead.setSender(old.getSender());
            newInvHead.setReceiver_Name(old.getReceiver_Name());
            newInvHead.setStatus(old.getStatus());
            newInvHead.setInv_No(old.getInv_No());
            newInvHead.setInv_Date(old.getInv_Date());
            newInvHead.setTotal(old.getTotal());
            newInvHead.setInvh_amt(old.getInvh_amt());
            newInvHead.setVat(old.getVat());
            newInvHead.setAfter_vat(old.getAfter_vat());
            newInvHead.setOther_tax(old.getOther_tax());
            newInvHead.setSummary(old.getSummary());
            Head.add(newInvHead);
        }
        
        if (!Head.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            List<Company> companys = companyRepository.findById2("Company");
            log.info("LOGGG"+companys);
            Company company = companys.get(0);
            log.info("TESTTTTTTT"+company);
            params.put("CNAME_TH", company.getName_th());
            params.put("CNAME_EN", company.getName_en());
            params.put("CADDRESS_TH1", company.getAddress_th1());
            params.put("CADDRESS_TH2", company.getAddress_th2());
            params.put("CADDRESS_EN1", company.getAddress_en1());
            params.put("CADDRESS_EN2", company.getAddress_en2());
            params.put("CTEL", company.getTel());
            params.put("CEMAIL", company.getEmail());
            params.put("CTAXID", company.getTaxId());
            params.put("URL", ReportUtils.getLogo());
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getSummary().doubleValue()));
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("invhreportCopy.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
    public String genInvHeadCopy2(InvHeadReport request) {

    	
        List<vinvhead> Head = vinvheadRepository.findDataReport(request.getInvHead());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vinvhead old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vinvhead();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vinvhead newInvHead = new vinvhead();

        	newInvHead.setProject(old.getProject());
            newInvHead.setSender(old.getSender());
            newInvHead.setReceiver_Name(old.getReceiver_Name());
            newInvHead.setStatus(old.getStatus());
            newInvHead.setInv_No(old.getInv_No());
            newInvHead.setInv_Date(old.getInv_Date());
            newInvHead.setTotal(old.getTotal());
            newInvHead.setInvh_amt(old.getInvh_amt());
            newInvHead.setVat(old.getVat());
            newInvHead.setAfter_vat(old.getAfter_vat());
            newInvHead.setOther_tax(old.getOther_tax());
            newInvHead.setSummary(old.getSummary());
            Head.add(newInvHead);
        }
        
        if (!Head.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            List<Company> companys = companyRepository.findById2("Company");
            log.info("LOGGG"+companys);
            Company company = companys.get(0);
            log.info("TESTTTTTTT"+company);
            params.put("CNAME_TH", company.getName_th());
            params.put("CNAME_EN", company.getName_en());
            params.put("CADDRESS_TH1", company.getAddress_th1());
            params.put("CADDRESS_TH2", company.getAddress_th2());
            params.put("CADDRESS_EN1", company.getAddress_en1());
            params.put("CADDRESS_EN2", company.getAddress_en2());
            params.put("CTEL", company.getTel());
            params.put("CEMAIL", company.getEmail());
            params.put("CTAXID", company.getTaxId());
            params.put("URL", ReportUtils.getLogo());
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getSummary().doubleValue()));
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("invhreportCopy2.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
}
