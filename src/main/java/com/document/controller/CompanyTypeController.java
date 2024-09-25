package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.entity.CompanyType;

import com.document.model.CompanyTypeRequest;
import com.document.model.CompanyTypeResponse;
import com.document.repository.AppUserRepository;

import com.document.repository.CompanyTypeRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;

import com.document.service.CompanyTypeService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/CompanyType")
public class CompanyTypeController {

    @Autowired
    CompanyTypeService companyTypeService;

    @Autowired
    CompanyTypeRepository companyTypeRepository;
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCompany(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "") String name){
	    Page<CompanyType> list = companyTypeService.findAllByCriteria(page, size, order, sort, name);
	    List<CompanyTypeResponse> contents = companyTypeService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long companyTypeId) {
        BaseResponse response = BaseResponse.builder().data(companyTypeRepository.findById(companyTypeId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompanyType(@Valid @RequestBody CompanyTypeRequest payload) {
        log.info("CompanyType::add Request : {}", payload);
        companyTypeService.createCompanyType(payload);
    }
	
    @DeleteMapping("/{companyTypeId}")
    public String delete(@PathVariable("companyTypeId") Long companyTypeId) {
    	companyTypeRepository.deleteById(companyTypeId);
        log.info(" -======= DELETE THIS ID ====" + companyTypeId);
        return "Delete param success";
    }


}
