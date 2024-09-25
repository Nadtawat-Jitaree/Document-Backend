package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.Company;
import com.document.model.CompanyRequest;
import com.document.model.CompanyResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.CompanyRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.CompanyService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;
  
    
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
            @RequestParam(required = false, defaultValue = "") String company_type,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String name_th,
            @RequestParam(required = false, defaultValue = "") String status){
	    Page<Company> list = companyService.findAllByCriteria(page, size, order, sort,company_type, name, name_th,status);
	    List<CompanyResponse> contents = companyService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long companyId) {
        BaseResponse response = BaseResponse.builder().data(companyRepository.findById(companyId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@Valid @RequestBody CompanyRequest payload) {
        log.info("Company::add Request : {}", payload);
        companyService.createCompany(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody CompanyRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			companyService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{companyId}")
    public String delete(@PathVariable("companyId") Long companyId) {
    	companyRepository.deleteById(companyId);
        log.info(" -======= DELETE THIS ID ====" + companyId);
        return "Delete param success";
    }


}
