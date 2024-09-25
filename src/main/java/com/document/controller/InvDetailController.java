package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.InvDetail;
import com.document.model.InvDetailRequest;
import com.document.model.InvDetailResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.InvDetailRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.InvDetailService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/InvDetail")
public class InvDetailController {

    @Autowired
    InvDetailService invDetailService;

    @Autowired
    InvDetailRepository invDetailRepository;
  
    
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
            @RequestParam(required = false, defaultValue = "0") Long quotation,
            @RequestParam(required = false, defaultValue = "0") Long invHead,
            @RequestParam(required = false, defaultValue = "0") Long receipt){
	    Page<InvDetail> list = invDetailService.findAllByCriteria(page, size, order, sort, quotation, invHead,receipt);
	    List<InvDetailResponse> contents = invDetailService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long invDetailId) {
        BaseResponse response = BaseResponse.builder().data(invDetailRepository.findById(invDetailId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addInvDetail(@Valid @RequestBody InvDetailRequest payload) {
        log.info("InvDetail::add Request : {}", payload);
        invDetailService.createInvDetail(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody InvDetailRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			invDetailService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{invDetailId}")
    public String delete(@PathVariable("invDetailId") Long invDetailId) {
    	invDetailRepository.deleteById(invDetailId);
        log.info(" -======= DELETE THIS ID ====" + invDetailId);
        return "Delete param success";
    }


}
