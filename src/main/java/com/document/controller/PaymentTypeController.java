package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.PaymentType;
import com.document.model.PaymentTypeRequest;
import com.document.model.PaymentTypeResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.PaymentTypeRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.PaymentTypeService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/PaymentType")
public class PaymentTypeController {

    @Autowired
    PaymentTypeService paymentTypeService;

    @Autowired
    PaymentTypeRepository paymentTypeRepository;
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllInvDetail(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String status){
	    Page<PaymentType> list = paymentTypeService.findAllByCriteria(page, size, order, sort, name, status);
	    List<PaymentTypeResponse> contents = paymentTypeService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long paymentTypeId) {
        BaseResponse response = BaseResponse.builder().data(paymentTypeRepository.findById(paymentTypeId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPaymentType(@Valid @RequestBody PaymentTypeRequest payload) {
        log.info("PaymentType::add Request : {}", payload);
        paymentTypeService.createPaymentType(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody PaymentTypeRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			paymentTypeService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{paymentTypeId}")
    public String delete(@PathVariable("paymentTypeId") Long paymentTypeId) {
    	paymentTypeRepository.deleteById(paymentTypeId);
        log.info(" -======= DELETE THIS ID ====" + paymentTypeId);
        return "Delete param success";
    }


}
