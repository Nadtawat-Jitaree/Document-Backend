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

import com.document.entity.InvDetail;
import com.document.entity.InvHead;
import com.document.entity.Quotation;
import com.document.entity.Receipt;
import com.document.model.InvDetailRequest;
import com.document.model.InvDetailResponse;
import com.document.repository.InvDetailRepository;
import com.document.repository.InvHeadRepository;
import com.document.repository.QuotationRepository;
import com.document.repository.ReceiptRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class InvDetailService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    InvDetailRepository invDetailRepository;

    @Autowired
    QuotationRepository quotationRepository;
    
    @Autowired
    InvHeadRepository invHeadRepository;
    
    @Autowired
    ReceiptRepository receiptRepository;
    
    public Page<InvDetail> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,Long quotation, Long invHead, Long receipt){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return invDetailRepository.findAllByCriteria( quotation, invHead,receipt, pageable);
    }
    
    
    @Transactional
    public List<InvDetailResponse> addFieldDesc(List<InvDetail> contents) {
        List<InvDetailResponse> response = new ArrayList<>();
        for(InvDetail content:contents){
        	InvDetailResponse invDetailResponse = InvDetailResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .name(content.getName())
                    .num(content.getNum())
                    .inv_amt(content.getInv_amt())
                    .discount(content.getDiscount())
                    .pay(content.getPay())
                    .build();
        	
            if (content.getQuotation() != null){
            	Quotation quotation = quotationRepository.findById(content.getQuotation().getId());
            	invDetailResponse.setQuotationId(quotation.getId());

            }
            
            if (content.getInvHead() != null){
            	InvHead invHead = invHeadRepository.findById(content.getInvHead().getId());
            	invDetailResponse.setInvHeadId(invHead.getId());

            }
            
            if (content.getReceipt() != null){
            	Receipt receipt = receiptRepository.findById(content.getReceipt().getId());
            	invDetailResponse.setReceiptId(receipt.getId());

            }
        	
            response.add(invDetailResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createInvDetail(InvDetailRequest payload) {
    	InvDetail invDetail= new InvDetail();
    	invDetail.setCreatedBy(authUtil.getUsernameFromSession());
    	invDetail.setCreatedDate(new Date());
    	invDetail.setName(payload.getName());
    	invDetail.setNum(payload.getNum());
    	invDetail.setInv_amt(payload.getInv_amt());
    	invDetail.setDiscount(payload.getDiscount());
    	invDetail.setPay(payload.getPay());
    	invDetail.setInvHead(payload.getInvHead());
    	invDetail.setQuotation(payload.getQuotation());
    	invDetail.setReceipt(payload.getReceipt());
        
    	invDetailRepository.save(invDetail);
     }
    
	public void update(Long id, InvDetailRequest req) {
		InvDetail old = invDetailRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setCreatedBy(authUtil.getUsernameFromSession());
		old.setCreatedDate(new Date());
		old.setName(req.getName());
		old.setNum(req.getNum());
		old.setInv_amt(req.getInv_amt());
		old.setDiscount(req.getDiscount());
		old.setPay(req.getPay());

    	invDetailRepository.save(old);
	}
    
}
