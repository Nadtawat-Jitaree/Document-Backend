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

import com.document.entity.PaymentType;
import com.document.model.PaymentTypeRequest;
import com.document.model.PaymentTypeResponse;
import com.document.repository.PaymentTypeRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class PaymentTypeService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    PaymentTypeRepository paymemntTypeRepository;
    
    public Page<PaymentType> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,String name, String status){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return paymemntTypeRepository.findAllByCriteria( name, status, pageable);
    }
    
    
    @Transactional
    public List<PaymentTypeResponse> addFieldDesc(List<PaymentType> contents) {
        List<PaymentTypeResponse> response = new ArrayList<>();
        for(PaymentType content:contents){
        	PaymentTypeResponse invDetailResponse = PaymentTypeResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .name(content.getName())
                    .status(content.getStatus())
                    .build();
        	
            response.add(invDetailResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createPaymentType(PaymentTypeRequest payload) {
    	PaymentType paymentType= new PaymentType();
    	paymentType.setCreatedBy(authUtil.getUsernameFromSession());
    	paymentType.setCreatedDate(new Date());
    	paymentType.setName(payload.getName());
    	paymentType.setStatus(payload.getStatus());
        
    	paymemntTypeRepository.save(paymentType);
     }
    
	public void update(Long id, PaymentTypeRequest req) {
		PaymentType old = paymemntTypeRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(new Date());
		old.setName(req.getName());
		old.setStatus(req.getStatus());

		paymemntTypeRepository.save(old);
	}
    
}
