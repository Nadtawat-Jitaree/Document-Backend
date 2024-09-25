package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.Payment;
import com.document.model.PaymentRequest;
import com.document.model.PaymentResponse;
import com.document.repository.PaymentRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.PaymentService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;
    
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllPayment(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "0") Long receipt){
	    Page<Payment> list = paymentService.findAllByCriteria(page, size, order, sort, receipt);
	    List<PaymentResponse> contents = paymentService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long paymentId) {
        BaseResponse response = BaseResponse.builder().data(paymentRepository.findById(paymentId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPaymentId(@Valid @RequestBody PaymentRequest payload) {
        log.info("Payment::add Request : {}", payload);
        paymentService.createPayment(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody PaymentRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			paymentService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{paymentId}")
    public String delete(@PathVariable("paymentId") Long paymentId) {
    	paymentRepository.deleteById(paymentId);
        log.info(" -======= DELETE THIS ID ====" + paymentId);
        return "Delete param success";
    }


}
