package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.InvHead;
import com.document.model.InvHeadReport;
import com.document.model.InvHeadRequest;
import com.document.model.InvHeadResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.InvHeadRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.InvHeadService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/InvHead")
public class InvHeadController {

    @Autowired
    InvHeadService invHeadService;

    @Autowired
    InvHeadRepository invHeadRepository;
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllInvHead(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "inv_Date") String sort,
            @RequestParam(required = false, defaultValue = "") String inv_No,
            @RequestParam(required = false, defaultValue = "") String inv_DateFr,
            @RequestParam(required = false, defaultValue = "") String inv_DateTo,
            @RequestParam(required = false, defaultValue = "") String project,
            @RequestParam(required = false, defaultValue = "") String sender,
            @RequestParam(required = false, defaultValue = "") String receiver_Name,
            @RequestParam(required = false, defaultValue = "") String status){
	    Page<InvHead> list = invHeadService.findAllByCriteria(page, size, order, sort, inv_No, inv_DateFr,inv_DateTo,project,sender,receiver_Name,status);
	    List<InvHeadResponse> contents = invHeadService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long invHeadId) {
        BaseResponse response = BaseResponse.builder().data(invHeadRepository.findById(invHeadId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addInvHead(@Valid @RequestBody InvHeadRequest payload) {
        log.info("InvHead::add Request : {}", payload);
        invHeadService.createInvHead(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody InvHeadRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			invHeadService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{invHeadId}")
    public String delete(@PathVariable("invHeadId") Long invHeadId) {
    	invHeadRepository.deleteById(invHeadId);
        log.info(" -======= DELETE THIS ID ====" + invHeadId);
        return "Delete param success";
    }

    @PostMapping("/export/invhead")
    public ResponseEntity<String> exportAccount(@RequestBody InvHeadReport request) {

        String reportRes = null;
        switch (request.getReportId()) {
            case "vinvhead":
                reportRes = invHeadService.genInvHead(request);
                break;
            case "vinvheadCopy":
                reportRes = invHeadService.genInvHeadCopy(request);
                break;
            case "vinvheadCopy2":
                reportRes = invHeadService.genInvHeadCopy2(request);
                break;
        }
        return new ResponseEntity<String>(reportRes,null, HttpStatus.OK);
   
    }

}
