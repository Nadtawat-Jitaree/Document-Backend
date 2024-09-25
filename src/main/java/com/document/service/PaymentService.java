package com.document.service;

import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.Payment;
import com.document.model.PaymentRequest;
import com.document.model.PaymentResponse;
import com.document.repository.PaymentRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    PaymentRepository paymentRepository;
    
    public Page<Payment> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,Long receipt){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return paymentRepository.findAllByCriteria( receipt, pageable);
    }
    
    
    @Transactional
    public List<PaymentResponse> addFieldDesc(List<Payment> contents) {
        List<PaymentResponse> response = new ArrayList<>();
        for(Payment content:contents){
        	PaymentResponse paymentResponse = PaymentResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .total(content.getTotal())
                    .payment_amt(content.getPayment_amt())
                    .vat(content.getVat())
                    .summary(content.getSummary())
                    .bank_name(content.getBank_name())
                    .bank_id(content.getBank_id())
                    .bank_date(content.getBank_date())
                    .payment_type(content.getPayment_type())
                    .receipt(content.getReceipt())
                    .status(content.getStatus())
                    .build();

        	
            response.add(paymentResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createPayment(PaymentRequest payload) {
    	Payment payment= new Payment();
    	payment.setCreatedBy(authUtil.getUsernameFromSession());
    	payment.setCreatedDate(new Date());
    	payment.setTotal(payload.getTotal());
    	Double PAYAMT = (payload.getPayment_amt());
    	payment.setPayment_amt(PAYAMT);
    	Double VAT = (PAYAMT*7/100);
    	payment.setVat(VAT);
    	payment.setSummary(PAYAMT+VAT);
    	payment.setBank_name(payload.getBank_name());
    	payment.setBank_id(payload.getBank_id());
    	payment.setBank_date(payload.getBank_date());
    	payment.setPayment_type(payload.getPayment_type());
    	payment.setReceipt(payload.getReceipt());
    	payment.setStatus(payload.getStatus());
        
    	paymentRepository.save(payment);
     }
    
	public void update(Long id, PaymentRequest req) {
		Payment old = paymentRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(new Date());
		old.setTotal(req.getTotal());
		Double PAYAMT = (req.getPayment_amt());
		old.setPayment_amt(PAYAMT);
		Double VAT = (PAYAMT*7/100);
    	old.setVat(VAT);
    	old.setSummary(PAYAMT+VAT);
    	old.setBank_name(req.getBank_name());
    	old.setBank_id(req.getBank_id());
    	old.setBank_date(req.getBank_date());
    	old.setPayment_type(req.getPayment_type());
    	old.setReceipt(req.getReceipt());
    	old.setStatus(req.getStatus());

    	paymentRepository.save(old);
	}
    
}
