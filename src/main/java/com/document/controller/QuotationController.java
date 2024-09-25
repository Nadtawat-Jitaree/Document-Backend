package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.Quotation;
import com.document.model.QuotationReport;
import com.document.model.QuotationRequest;
import com.document.model.QuotationResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.QuotationRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.QuotationService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Quotation")
public class QuotationController {

    @Autowired
    QuotationService quotationService;

    @Autowired
    QuotationRepository quotationRepository;
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllQuotation(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "qt_Date") String sort,
            @RequestParam(required = false, defaultValue = "") String qt_No,
            @RequestParam(required = false, defaultValue = "") String qt_DateFr,
            @RequestParam(required = false, defaultValue = "") String qt_DateTo,
            @RequestParam(required = false, defaultValue = "") String project,
            @RequestParam(required = false, defaultValue = "") String prs_Name,
            @RequestParam(required = false, defaultValue = "") String rec_Name,
            @RequestParam(required = false, defaultValue = "") String status){
	    Page<Quotation> list = quotationService.findAllByCriteria(page, size, order, sort, qt_No,qt_DateFr,qt_DateTo,project,prs_Name,rec_Name,status);
	    List<QuotationResponse> contents = quotationService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long quotationId) {
        BaseResponse response = BaseResponse.builder().data(quotationRepository.findById(quotationId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addQuotation(@Valid @RequestBody QuotationRequest payload) {
        log.info("Quotation::add Request : {}", payload);
        quotationService.createQuotation(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody QuotationRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			quotationService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{quotationId}")
    public String delete(@PathVariable("quotationId") Long quotationId) {
    	quotationRepository.deleteById(quotationId);
        log.info(" -======= DELETE THIS ID ====" + quotationId);
        return "Delete param success";
    }

    @PostMapping("/export/quotation")
    public ResponseEntity<String> exportAccount(@RequestBody QuotationReport request) {

        String reportRes = null;
        switch (request.getReportId()) {
            case "vquotation":
                reportRes = quotationService.genQuotation(request);
                break;
            case "vquotationcopy":
                reportRes = quotationService.genQuotationCopy(request);
                break;
            case "vquotationcopy2":
                reportRes = quotationService.genQuotationCopy2(request);
                break;
        }
        return new ResponseEntity<String>(reportRes,null, HttpStatus.OK);
   
    }

}
