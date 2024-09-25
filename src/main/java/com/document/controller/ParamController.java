package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.Param;
import com.document.model.ParamRequest;
import com.document.model.ParamResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.ParamRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.ParamService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Param")
public class ParamController {

    @Autowired
    ParamService paramService;

    @Autowired
    ParamRepository paramRepository;
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllParam(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "") String paramCode,
            @RequestParam(required = false, defaultValue = "") String paramType){
	    Page<Param> list = paramService.findAllByCriteria(page, size, order, sort, paramCode,paramType);
	    List<ParamResponse> contents = paramService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long paramId) {
        BaseResponse response = BaseResponse.builder().data(paramRepository.findById(paramId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addParam(@Valid @RequestBody ParamRequest payload) {
        log.info("Param::add Request : {}", payload);
        paramService.createParam(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody ParamRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			paramService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    
    @DeleteMapping("/{paramId}")
    public String delete(@PathVariable("paramId") Long paramId) {
    	paramRepository.deleteById(paramId);
        log.info(" -======= DELETE THIS ID ==== " + paramId);
        return "Delete param success";
    }


}
