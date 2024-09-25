package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.Receipt;
import com.document.model.ReceiptReport;
import com.document.model.ReceiptRequest;
import com.document.model.ReceiptResponse;
import com.document.repository.AppUserRepository;
import com.document.repository.ReceiptRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.ReceiptService;
import com.document.utility.AuthUtil;

import java.util.List;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Receipt")
public class ReceiptController {

    @Autowired
    ReceiptService receiptService;

    @Autowired
    ReceiptRepository receiptRepository;
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllReceipt(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "receipt_Date") String sort,
            @RequestParam(required = false, defaultValue = "") String receipt_No,
            @RequestParam(required = false, defaultValue = "") String receipt_DateFr,
            @RequestParam(required = false, defaultValue = "") String receipt_DateTo,
            @RequestParam(required = false, defaultValue = "") String project,
            @RequestParam(required = false, defaultValue = "") String payment_Name,
            @RequestParam(required = false, defaultValue = "") String rec_Name,
            @RequestParam(required = false, defaultValue = "") String status){
	    Page<Receipt> list = receiptService.findAllByCriteria(page, size, order, sort, receipt_No, receipt_DateFr,receipt_DateTo,project,payment_Name,rec_Name,status);
	    List<ReceiptResponse> contents = receiptService.addFieldDesc(list.getContent());
	    BasePagingResponse response = BasePagingResponse.builder().data(contents).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
	    return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long receiptId) {
        BaseResponse response = BaseResponse.builder().data(receiptRepository.findById(receiptId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addInvHead(@Valid @RequestBody ReceiptRequest payload) {
        log.info("Receipt::add Request : {}", payload);
        receiptService.createReceipt(payload);
    }
    
	@PutMapping("{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody ReceiptRequest req) {
		System.out.println("Update--------------------------------------------");
		try {
			receiptService.update(id, req);

			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(ResponseConst.RESPONSE_MSG_FAIL, null, HttpStatus.BAD_REQUEST);
		}
	}
	
    @DeleteMapping("/{receiptId}")
    public String delete(@PathVariable("receiptId") Long receiptId) {
    	receiptRepository.deleteById(receiptId);
        log.info(" -======= DELETE THIS ID ====" + receiptId);
        return "Delete param success";
    }

    @PostMapping("/export/receipt")
    public ResponseEntity<String> exportAccount(@RequestBody ReceiptReport request) {

        String reportRes = null;
        switch (request.getReportId()) {
            case "vreceipt":
                reportRes = receiptService.genReceipt(request);
                break;
            case "vreceiptCopy":
                reportRes = receiptService.genReceiptCopy(request);
                break;
            case "vreceiptCopy2":
                reportRes = receiptService.genReceiptCopy2(request);
                break;
        }
        return new ResponseEntity<String>(reportRes,null, HttpStatus.OK);
   
    }
}
