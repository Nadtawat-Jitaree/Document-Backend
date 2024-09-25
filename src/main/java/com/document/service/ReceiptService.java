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
import com.document.entity.Payment;
import com.document.entity.PaymentType;
import com.document.entity.Receipt;
import com.document.entity.RunningParam;
import com.document.entity.views.vreceipt;
import com.document.model.ReceiptReport;
import com.document.model.ReceiptRequest;
import com.document.model.ReceiptResponse;
import com.document.repository.CompanyRepository;
import com.document.repository.InvDetailRepository;
import com.document.repository.PaymentRepository;
import com.document.repository.PaymentTypeRepository;
import com.document.repository.ReceiptRepository;
import com.document.repository.RunningParamRepository;
import com.document.repository.VreceiptRepository;
import com.document.utility.AuthUtil;
import com.document.utility.NumberUtils;
import com.document.utility.ReportUtils;

@Slf4j
@Service
public class ReceiptService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    CompanyRepository companyRepository;
    
    @Autowired
    RunningParamRepository runningParamRepository;
    
    @Autowired
    InvDetailRepository invDetailRepository;
    
    @Autowired
    PaymentRepository paymentRepository;
    
    @Autowired
    PaymentTypeRepository paymentTypeRepository;
    
    @Autowired
    VreceiptRepository vreceiptRepository;
    
	@Autowired
	ReportService reportService;
    
    public Page<Receipt> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,String receipt_No, String receipt_DateFr, String receipt_DateTo,String project,String payment_Name,String rec_Name,String status){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return receiptRepository.findAllByCriteria( receipt_No, receipt_DateFr,receipt_DateTo,project,payment_Name,rec_Name,status, pageable);
    }
    
    @Transactional
    public List<ReceiptResponse> addFieldDesc(List<Receipt> contents) {
        List<ReceiptResponse> response = new ArrayList<>();
        for(Receipt content:contents){
        	ReceiptResponse receiptResponse = ReceiptResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .receipt_No(content.getReceipt_No())
                    .receipt_Date(content.getReceipt_Date())
                    .total(content.getTotal())
                    .receipt_amt(content.getReceipt_amt())
                    .vat(content.getVat())
                    .after_vat(content.getAfter_vat())
                    .project(content.getProject())
                    .payment_Name(content.getPayment_Name())
                    .rec_Name(content.getRec_Name())
                    .status(content.getStatus())
                    .build();
        	
            if (content.getCompany() != null){
            	Company company = companyRepository.findById(content.getCompany().getId());
            	receiptResponse.setCompanyId(company.getId());

            }
        	
            response.add(receiptResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createReceipt(ReceiptRequest payload) {
    	Receipt receipt= new Receipt();
    	receipt.setCreatedBy(authUtil.getUsernameFromSession());
    	receipt.setCreatedDate(new Date());
    	receipt.setReceipt_Date(payload.getReceipt_Date());
    	receipt.setTotal(payload.getTotal());
    	Double RETAMT = (payload.getReceipt_amt());
    	receipt.setReceipt_amt(RETAMT);
    	Double VAT = (RETAMT*7/100);
    	receipt.setVat(VAT);
    	Double SUM = (RETAMT+VAT);
    	receipt.setAfter_vat(SUM);
    	receipt.setCompany(payload.getCompany());
    	receipt.setProject(payload.getProject());
    	receipt.setPayment_Name(payload.getPayment_Name());
    	receipt.setRec_Name(payload.getRec_Name());
    	receipt.setStatus(payload.getStatus());
        
        RunningParam runningParam = runningParamRepository.findByParamCode(payload.getParam());
        if(runningParam == null) { 	
            runningParam = new RunningParam();
            runningParam.setParamCode(payload.getParam());
            runningParam.setParamValue(0);
            runningParamRepository.save(runningParam);
        }
        runningParam.setParamValue(runningParam.getParamValue() + 1);
        runningParamRepository.save(runningParam);
        

        int year = payload.getReceipt_Date().getYear() % 100;
        receipt.setReceipt_No(payload.getParam()+(year+43)+String.format("%04d", runningParam.getParamValue()));
        receiptRepository.save(receipt);
        
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
        	old.setInvHead(old.getInvHead());
        	old.setQuotation(old.getQuotation());
        	old.setReceipt(receipt);
        	
        	detail.add(old);
        	
        }
    	invDetailRepository.saveAll(detail);
    	
    	Payment payment= new Payment();
    	payment.setCreatedBy(authUtil.getUsernameFromSession());
    	payment.setCreatedDate(new Date());
    	payment.setTotal(payload.getTotal());
    	Double PAYAMT = (payload.getPayment_amt());
    	payment.setPayment_amt(PAYAMT);
    	Double VAT1 = (PAYAMT*7/100);
    	payment.setVat(VAT1);
    	Double OTT = (PAYAMT*3/100);
    	payment.setSummary(PAYAMT+VAT1-OTT);
    	payment.setBank_name(payload.getBank_name());
    	payment.setBank_id(payload.getBank_id());
    	payment.setBank_date(payload.getBank_date());
    	
    	PaymentType paymentType = paymentTypeRepository.findById(payload.getPayment_type());
    	payment.setPayment_type(paymentType);
    	payment.setReceipt(receipt);
    	payment.setStatus("Active");
        
    	paymentRepository.save(payment);

        
    	
     }
    
	public void update(Long id, ReceiptRequest req) {
		Receipt old = receiptRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(old.getCreatedDate());
		old.setReceipt_No(old.getReceipt_No());
		if(req.getReceipt_Date()==null) {
			old.setReceipt_Date(old.getReceipt_Date());
		}else {
			old.setReceipt_Date(req.getReceipt_Date());
		}
		
		if(req.getTotal()==null) {
			old.setTotal(old.getTotal());
		}else {
			old.setTotal(req.getTotal());
		}

		Double RETAMT = null;
		
		if(req.getReceipt_amt()==null) {
			old.setReceipt_amt(old.getReceipt_amt());
			RETAMT = old.getReceipt_amt();
		}else {
			RETAMT = (req.getReceipt_amt());
			old.setReceipt_amt(RETAMT);
		}
		
		Double VAT = (RETAMT*7/100);
		old.setVat(VAT);
		Double SUM = (RETAMT+VAT);
		old.setAfter_vat(SUM);
    	if(req.getCompany()==null) {
    		old.setCompany(old.getCompany());
    	}else {
    		old.setCompany(req.getCompany());
    	}
    	if(req.getProject()==null) {
    		old.setProject(old.getProject());
    	}else {
        	old.setProject(req.getProject());
    	}
    	if(req.getPayment_Name()==null) {
    		old.setPayment_Name(old.getPayment_Name());
    	}else {
        	old.setPayment_Name(req.getPayment_Name());
    	}
    	if(req.getRec_Name()==null) {
    		old.setRec_Name(old.getRec_Name());
    	}else {
    		old.setRec_Name(req.getRec_Name());
    	}
    	if(req.getStatus()==null) {
    		old.setStatus(old.getStatus());
    	}else {
    		old.setStatus(req.getStatus());
    	}
    	
    	receiptRepository.save(old);
    	
    	if(req.getPayment_id() != null) {
    		Payment old1 = paymentRepository.findById(req.getPayment_id());
    		old1.setUpdatedBy(authUtil.getUsernameFromSession());
    		old1.setUpdatedDate(new Date());
    		old1.setCreatedBy(authUtil.getUsernameFromSession());
    		old1.setCreatedDate(new Date());
    		if(req.getTotal()==null) {
    			old1.setTotal(old1.getTotal());
    		}else {
    			old1.setTotal(req.getTotal());
    		}
    		Double PAYAMT = null;
    		if(req.getPayment_amt()==null) {
    			PAYAMT = (old1.getPayment_amt());
    		}else {
    			PAYAMT = (req.getPayment_amt());
    		}
    		old1.setPayment_amt(PAYAMT);
    		Double VAT1 = (PAYAMT*7/100);
        	old1.setVat(VAT1);
        	old1.setSummary(PAYAMT+VAT1);
        	if(req.getBank_name()==null) {
        		old1.setBank_name(old1.getBank_name());
        	}else {
        		old1.setBank_name(req.getBank_name());
        	}
        	if(req.getBank_id()==null) {
        		old1.setBank_id(old1.getBank_id());
        	}else {
        		old1.setBank_id(req.getBank_id());
        	}
        	if(req.getBank_date()==null) {
        		old1.setBank_date(old1.getBank_date());
        	}else {
        		old1.setBank_date(req.getBank_date());
        	}
        	PaymentType paymentType = paymentTypeRepository.findById(req.getPayment_type());
        	if(req.getPayment_type()==null) {
        		old1.setPayment_type(old1.getPayment_type());
        	}else {
        		old1.setPayment_type(paymentType);
        	}
        	old1.setReceipt(old1.getReceipt());
        	if(req.getStatus()==null) {
        		old1.setStatus(old1.getStatus());
        	}else {
        		old1.setStatus(req.getStatus());
        	}


        	paymentRepository.save(old1);
    	}
	}
	
    public String genReceipt(ReceiptReport request) {

    	
        List<vreceipt> Head = vreceiptRepository.findDataReport(request.getReceipt());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vreceipt old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vreceipt();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vreceipt newReceipt = new vreceipt();

        	newReceipt.setProject(old.getProject());
        	newReceipt.setPayment_Name(old.getPayment_Name());
        	newReceipt.setRec_Name(old.getRec_Name());
        	newReceipt.setStatus(old.getStatus());
        	newReceipt.setReceipt_No(old.getReceipt_No());
        	newReceipt.setReceipt_Date(old.getReceipt_Date());
        	newReceipt.setTotal(old.getTotal());
        	newReceipt.setReceipt_amt(old.getReceipt_amt());
        	newReceipt.setVat(old.getVat());
        	newReceipt.setAfter_vat(old.getAfter_vat());
            Head.add(newReceipt);
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
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getAfter_vat().doubleValue()));
            
            List<Payment> payments = paymentRepository.findById2(request.getReceipt());
            log.info("LOGGG"+payments);
            Payment payment = payments.get(0);
            params.put("PTOTAL", payment.getTotal());
            params.put("PPAYMENT_AMT", payment.getPayment_amt());
            params.put("PSUMMARY", payment.getSummary());
            params.put("BANK_NAME", payment.getBank_name());
            params.put("BANK_ID", payment.getBank_id());
            params.put("BANK_DATE", payment.getBank_date());
            
            List<PaymentType> paymentTypes = paymentTypeRepository.findById2(payment.getPayment_type().getId());
            log.info("LOGGG"+paymentTypes);
            PaymentType paymentType = paymentTypes.get(0);
            params.put("PTNAME", paymentType.getName());
            params.put("PTSTATUS", paymentType.getStatus());
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("receiptReport.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    public String genReceiptCopy(ReceiptReport request) {

    	
        List<vreceipt> Head = vreceiptRepository.findDataReport(request.getReceipt());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vreceipt old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vreceipt();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vreceipt newReceipt = new vreceipt();

        	newReceipt.setProject(old.getProject());
        	newReceipt.setPayment_Name(old.getPayment_Name());
        	newReceipt.setRec_Name(old.getRec_Name());
        	newReceipt.setStatus(old.getStatus());
        	newReceipt.setReceipt_No(old.getReceipt_No());
        	newReceipt.setReceipt_Date(old.getReceipt_Date());
        	newReceipt.setTotal(old.getTotal());
        	newReceipt.setReceipt_amt(old.getReceipt_amt());
        	newReceipt.setVat(old.getVat());
        	newReceipt.setAfter_vat(old.getAfter_vat());
            Head.add(newReceipt);
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
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getAfter_vat().doubleValue()));
            
            List<Payment> payments = paymentRepository.findById2(request.getReceipt());
            log.info("LOGGG"+payments);
            Payment payment = payments.get(0);
            params.put("PTOTAL", payment.getTotal());
            params.put("PPAYMENT_AMT", payment.getPayment_amt());
            params.put("PSUMMARY", payment.getSummary());
            params.put("BANK_NAME", payment.getBank_name());
            params.put("BANK_ID", payment.getBank_id());
            params.put("BANK_DATE", payment.getBank_date());
            
            List<PaymentType> paymentTypes = paymentTypeRepository.findById2(payment.getPayment_type().getId());
            log.info("LOGGG"+paymentTypes);
            PaymentType paymentType = paymentTypes.get(0);
            params.put("PTNAME", paymentType.getName());
            params.put("PTSTATUS", paymentType.getStatus());
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("receiptReportCopy.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
    public String genReceiptCopy2(ReceiptReport request) {

    	
        List<vreceipt> Head = vreceiptRepository.findDataReport(request.getReceipt());
        log.info("==============head " + Head);
        String exported = "";
       
        int requiredRows = 22;
        int currentRowCount = Head.size();
        
        vreceipt old = currentRowCount > 0 ? Head.get(currentRowCount - 1) : new vreceipt();
        for (int i = currentRowCount; i < requiredRows; i++) {
        	vreceipt newReceipt = new vreceipt();

        	newReceipt.setProject(old.getProject());
        	newReceipt.setPayment_Name(old.getPayment_Name());
        	newReceipt.setRec_Name(old.getRec_Name());
        	newReceipt.setStatus(old.getStatus());
        	newReceipt.setReceipt_No(old.getReceipt_No());
        	newReceipt.setReceipt_Date(old.getReceipt_Date());
        	newReceipt.setTotal(old.getTotal());
        	newReceipt.setReceipt_amt(old.getReceipt_amt());
        	newReceipt.setVat(old.getVat());
        	newReceipt.setAfter_vat(old.getAfter_vat());
            Head.add(newReceipt);
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
            
            
            params.put("TEXT_SUMMARY_TOTAL", NumberUtils.getText(Head.get(0).getAfter_vat().doubleValue()));
            
            List<Payment> payments = paymentRepository.findById2(request.getReceipt());
            log.info("LOGGG"+payments);
            Payment payment = payments.get(0);
            params.put("PTOTAL", payment.getTotal());
            params.put("PPAYMENT_AMT", payment.getPayment_amt());
            params.put("PSUMMARY", payment.getSummary());
            params.put("BANK_NAME", payment.getBank_name());
            params.put("BANK_ID", payment.getBank_id());
            params.put("BANK_DATE", payment.getBank_date());
            
            List<PaymentType> paymentTypes = paymentTypeRepository.findById2(payment.getPayment_type().getId());
            log.info("LOGGG"+paymentTypes);
            PaymentType paymentType = paymentTypes.get(0);
            params.put("PTNAME", paymentType.getName());
            params.put("PTSTATUS", paymentType.getStatus());
            
            JRBeanCollectionDataSource datas = new JRBeanCollectionDataSource(Head);
//            log.info("Data in JRBeanCollectionDataSource: " + datas.getData());
            log.info("Data in Params: " + params);
            
            exported = reportService.exportPDF("receiptReportCopy2.jrxml", params, datas);
        }
        log.info("==============exported " + exported);
        return exported;
    }
    
}
