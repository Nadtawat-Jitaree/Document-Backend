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
import com.document.entity.Quotation;
import com.document.entity.RunningParam;
import com.document.entity.views.vquotation;

import com.document.model.QuotationReport;
import com.document.model.QuotationRequest;
import com.document.model.QuotationResponse;

import com.document.repository.CompanyRepository;

import com.document.repository.InvDetailRepository;
import com.document.repository.QuotationRepository;
import com.document.repository.RunningParamRepository;
import com.document.repository.VquotationRepository;
import com.document.utility.AuthUtil;
import com.document.utility.NumberUtils;
import com.document.utility.ReportUtils;


@Slf4j
@Service
public class QuotationService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    CompanyRepository companyRepository;
    
    @Autowired
    RunningParamRepository runningParamRepository;
    
    @Autowired
    InvDetailRepository invDetailRepository;
    
    @Autowired
    VquotationRepository vquotationRepository;
    
	@Autowired
	ReportService reportService;
    
    public Page<Quotation> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String qt_No,String qt_DateFr,String qt_DateTo,String project,String prs_Name,String rec_Name,String status){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return quotationRepository.findAllByCriteria( qt_No,qt_DateFr,qt_DateTo,project,prs_Name,rec_Name,status, pageable);
    }
    
    @Transactional
    public List<QuotationResponse> addFieldDesc(List<Quotation> contents) {
        List<QuotationResponse> response = new ArrayList<>();
        for(Quotation content:contents){
        	QuotationResponse quotationResponse = QuotationResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .qt_No(content.getQt_No())
                    .qt_Date(content.getQt_Date())
                    .total(content.getTotal())
                    .qt_amt(content.getQt_amt())
                    .vat(content.getVat())
                    .after_vat(content.getAfter_vat())
                    .other_tax(content.getOther_tax())
                    .summary(content.getSummary())
                    .project(content.getProject())
                    .prs_Name(content.getPrs_Name())
                    .rec_Name(content.getRec_Name())
                    .status(content.getStatus())
                    .build();
        	
            if (content.getCompany() != null){
            	Company company = companyRepository.findById(content.getCompany().getId());
            	quotationResponse.setCompanyId(company.getId());

            }
        	
            response.add(quotationResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createQuotation(QuotationRequest payload) {
    	Quotation quotation= new Quotation();
    	quotation.setCreatedBy(authUtil.getUsernameFromSession());
    	quotation.setCreatedDate(new Date());
    	quotation.setQt_Date(payload.getQt_Date());
    	quotation.setTotal(payload.getTotal());
    	Double QTAMT = (payload.getQt_amt());
    	quotation.setQt_amt(QTAMT);
    	Double VAT = (QTAMT*7/100);
    	quotation.setVat(VAT);
    	quotation.setAfter_vat(QTAMT+VAT);
    	Double WDT = (QTAMT*3/100);
    	quotation.setOther_tax(WDT);
    	Double SUM = (QTAMT+VAT-WDT);
    	quotation.setSummary(SUM);
	    	quotation.setCompany(payload.getCompany());
	    	quotation.setProject(payload.getProject());
	    	quotation.setPrs_Name(payload.getPrs_Name());
	    	quotation.setRec_Name(payload.getRec_Name());
	    	quotation.setStatus(payload.getStatus());
        
        RunningParam runningParam = runningParamRepository.findByParamCode(payload.getParam());
        if(runningParam == null) { 	
            runningParam = new RunningParam();
            runningParam.setParamCode(payload.getParam());
            runningParam.setParamValue(0);
            runningParamRepository.save(runningParam);
        }
        runningParam.setParamValue(runningParam.getParamValue() + 1);
        runningParamRepository.save(runningParam);
        

        int year = payload.getQt_Date().getYear() % 100;
        quotation.setQt_No(payload.getParam()+(year+43)+String.format("%04d", runningParam.getParamValue()));
    	quotationRepository.save(quotation);
    	
        List<InvDetail> detail = new ArrayList<>();
        
        
        for(InvDetail n : payload.getInvDetail()) {
        	InvDetail invDetail= new InvDetail();
        	invDetail.setCreatedBy(authUtil.getUsernameFromSession());
        	invDetail.setCreatedDate(new Date());
        	invDetail.setName(n.getName());
        	invDetail.setNum(n.getNum());
        	invDetail.setInv_amt(n.getInv_amt());
        	invDetail.setDiscount(n.getDiscount());
        	invDetail.setPay(n.getPay());
        	invDetail.setInvHead(n.getInvHead());
        	invDetail.setQuotation(quotation);
        	invDetail.setReceipt(n.getReceipt());
        	
        	detail.add(invDetail);
        	
        }
    	invDetailRepository.saveAll(detail);
     }
    
	public void update(Long id, QuotationRequest req) {
		Quotation old = quotationRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(old.getCreatedDate());
		old.setQt_No(old.getQt_No());
		if(req.getQt_Date()== null) {
			old.setQt_Date(old.getQt_Date());
		}else {
			old.setQt_Date(req.getQt_Date());
		}
		Double total=null;
		if(req.getTotal() == null) {
			old.setTotal(old.getTotal());
			total = old.getTotal();
		}else {
			old.setTotal(req.getTotal());
			
			total = req.getTotal();
		}
		
		Double qtamt=null;
		if(req.getQt_amt() == null) {
			old.setQt_amt(old.getQt_amt());
			qtamt = old.getQt_amt();
		}else {
			old.setQt_amt(req.getQt_amt());
			qtamt = req.getQt_amt();
		}
		
		Double QTAMT = (qtamt);
		old.setQt_amt(QTAMT);
		Double VAT = (QTAMT*7/100);
		old.setVat(VAT);
		old.setAfter_vat(QTAMT+VAT);
		Double WDT = (QTAMT*3/100);
		old.setOther_tax(WDT);
		Double SUM = (QTAMT+VAT-WDT);
    	old.setSummary(SUM);
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
    	if(req.getStatus() == null) {
    		old.setStatus(old.getStatus());
    	}else {
    		old.setStatus(req.getStatus());
    	}
		quotationRepository.save(old);
	}
	
    public String genQuotation(QuotationReport request) {

    	
        List<vquotation> Head = vquotationRepository.findDataReport(request.getQuotation());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        vquotation old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vquotation();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vquotation newQuotation = new vquotation();

            newQuotation.setProject(old.getProject());
            newQuotation.setPrs_Name(old.getPrs_Name());
            newQuotation.setRec_Name(old.getRec_Name());
            newQuotation.setStatus(old.getStatus());
            newQuotation.setQt_No(old.getQt_No());
            newQuotation.setQt_Date(old.getQt_Date());
            newQuotation.setTotal(old.getTotal());
            newQuotation.setQt_amt(old.getQt_amt());
            newQuotation.setVat(old.getVat());
            newQuotation.setAfter_vat(old.getAfter_vat());
            newQuotation.setOther_tax(old.getOther_tax());
            newQuotation.setSummary(old.getSummary());
            Head.add(newQuotation);
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
            
            exported = reportService.exportPDF("qtreport.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
    public String genQuotationCopy(QuotationReport request) {

    	
        List<vquotation> Head = vquotationRepository.findDataReport(request.getQuotation());
        log.info("==============head " + Head);
        String exported = "";

        int requiredRows = 22;
        int currentRowCount = Head.size();
        vquotation old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vquotation();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vquotation newQuotation = new vquotation();

            newQuotation.setProject(old.getProject());
            newQuotation.setPrs_Name(old.getPrs_Name());
            newQuotation.setRec_Name(old.getRec_Name());
            newQuotation.setStatus(old.getStatus());
            newQuotation.setQt_No(old.getQt_No());
            newQuotation.setQt_Date(old.getQt_Date());
            newQuotation.setTotal(old.getTotal());
            newQuotation.setQt_amt(old.getQt_amt());
            newQuotation.setVat(old.getVat());
            newQuotation.setAfter_vat(old.getAfter_vat());
            newQuotation.setOther_tax(old.getOther_tax());
            newQuotation.setSummary(old.getSummary());
            Head.add(newQuotation);
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
            
            exported = reportService.exportPDF("qtreportCopy.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
    public String genQuotationCopy2(QuotationReport request) {

    	
        List<vquotation> Head = vquotationRepository.findDataReport(request.getQuotation());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        vquotation old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vquotation();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vquotation newQuotation = new vquotation();

            newQuotation.setProject(old.getProject());
            newQuotation.setPrs_Name(old.getPrs_Name());
            newQuotation.setRec_Name(old.getRec_Name());
            newQuotation.setStatus(old.getStatus());
            newQuotation.setQt_No(old.getQt_No());
            newQuotation.setQt_Date(old.getQt_Date());
            newQuotation.setTotal(old.getTotal());
            newQuotation.setQt_amt(old.getQt_amt());
            newQuotation.setVat(old.getVat());
            newQuotation.setAfter_vat(old.getAfter_vat());
            newQuotation.setOther_tax(old.getOther_tax());
            newQuotation.setSummary(old.getSummary());
            Head.add(newQuotation);
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
            
            exported = reportService.exportPDF("qtreportCopy2.jrxml", params, datas);
        }
//        log.info("==============exported " + exported);
        return exported;
    }
    
//    public String genQuotation(QuotationReport request) {
//
//    	
//        List<vquotation> Head1 = vquotationRepository.findDataReport(request.getQt_No());
//        log.info("==============head1 " + Head1);
//        String exported = "";
//       
//        
//        if (!Head1.isEmpty()) {
//            Map<String, Object> params = new HashMap<>();
//            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head1);
//            log.info("Data in Params: " + params);
//            exported = reportService.exportPDF("qt.jrxml", params, datas);
//        }
//        log.info("==============exported " + exported);
//        return exported;
//    }
    
}
