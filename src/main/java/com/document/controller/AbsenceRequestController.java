package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.AbsenceRequest;
import com.document.entity.Employee;
import com.document.model.CreateEmplAbsence;
import com.document.repository.AbsenceRequestRepository;
import com.document.repository.EmployeeRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.respond.ServerResponse;
import com.document.service.AbsenceRequestService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;

import java.sql.SQLException;
import java.util.Date;
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/AbsenceRequest")
public class AbsenceRequestController {

    @Autowired
    AbsenceRequestService absenceRequestService;

    @Autowired
    AbsenceRequestRepository absenceRequestRepository;
    
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAbsence(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "employee.employeeNo") String sort,
            @RequestParam(required = false, defaultValue = "") String employeeNo,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String surName) {
        Page<AbsenceRequest> list = absenceRequestService.findAllByCriteria(page, size, order, sort, employeeNo, name, surName);
    	
    	BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
    	
    	return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
//
//    @GetMapping
//    public ResponseEntity<Object> get(@RequestParam String employeeNo) {
//        BaseResponse response = BaseResponse.builder().data(absenceRequestRepository.fin(employeeNo)).build();
//        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
//    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long employeeId) {
        BaseResponse response = BaseResponse.builder().data(absenceRequestRepository.findById2(employeeId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
   
    @PutMapping("/{requestId}")
    public ServerResponse updateParam(@PathVariable(value = "requestId") Long requestId,@RequestBody AbsenceRequest req) {
		 
    	System.out.println("Req:" + req.getId());
    	System.out.println("Req:" + req.getEmployee().getId());
    	
    	try {
    			System.out.println("Empl:" +  req.getEmployee().getId());
    			
    			AbsenceRequest absenceRequest = absenceRequestRepository.findById2(requestId);
    			
    			if (!req.getAbsType().equals(null))
    				absenceRequest.setAbsType(req.getAbsType());
    			if (!req.getAbsStart().equals(null))
    				absenceRequest.setAbsStart(req.getAbsStart());
    			if(!req.getAbsEnd().equals(null))
    				absenceRequest.setAbsEnd(req.getAbsEnd());
    			if(!req.getAbsDetail().equals(null))
    				absenceRequest.setAbsDetail(req.getAbsDetail());
    			if(!req.getAbsDays().equals(null))
    				absenceRequest.setAbsDays(req.getAbsDays());
    			absenceRequest.setUpdatedBy(authUtil.getUsernameFromSession());
    			absenceRequest.setUpdatedDate(new Date());
    			if(!req.getAbsStatus().equals(null)) absenceRequest.setAbsStatus(req.getAbsStatus());
    			//Store
//		        absenceRequestRepository.save(absenceRequest);
		        
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS,absenceRequestRepository.save(absenceRequest));
		   } catch (Exception e) {
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
	    		
		   }
		        
    } 

    @PostMapping
    public ServerResponse insertParam(@Valid @RequestBody CreateEmplAbsence req) {
		   try {
			 
		        AbsenceRequest newAbsence = new AbsenceRequest();
		        newAbsence.setCreatedBy(authUtil.getUsernameFromSession());
		        newAbsence.setCreatedDate(new Date());
		        newAbsence.setAbsType(req.getAbsType());
		        newAbsence.setAbsDetail(req.getAbsDetail());
		        newAbsence.setAbsDays(req.getAbsDays());
		        newAbsence.setAbsStatus(req.getStatus());
		        
		        newAbsence.setAbsStart(com.document.utility.DateUtils.convertStringToDate(req.getAbsStart(), "dd-MM-yyyy"));
		        newAbsence.setAbsEnd(com.document.utility.DateUtils.convertStringToDate(req.getAbsEnd(), "dd-MM-yyyy"));
			    //Emplyee
			    Employee employee = employeeRepository.findById(req.getEmployee().getId());
			    newAbsence.setEmployee(employee);
			    
		        absenceRequestRepository.save(newAbsence);
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,newAbsence);
		   } catch (Exception e) {
//			   Optional<Ou> ou = ouRepository.findById(req.getOu().getId());
//			    System.out.println("OU"+ou.isPresent());
			    
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
	    		
		   }    
	}

    @DeleteMapping("/{employeeId}")
    public ServerResponse deleteParam(@PathVariable(value = "employeeId") Long employeeId) {
           try {
        	    Employee delEmployee = employeeRepository.findById(employeeId);
        	    delEmployee.setStatus("Deleted");
//		        employeeRepository.deleteById(employeeId);
        	    delEmployee.setUpdatedBy(authUtil.getUsernameFromSession());
        	    delEmployee.setUpdatedDate(new Date());
        	    employeeRepository.save(delEmployee);
        	return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_DELETE_SUCCESS,delEmployee);
        			   
//		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_DELETE_SUCCESS,employeeId);
		   } catch (Exception ex) {
			   String msg = ex.getMessage();
		        if (ex.getCause().getCause() instanceof SQLException) {
		            SQLException e = (SQLException) ex.getCause().getCause();

		            if (e.getMessage().contains("Key")) {
		                msg = e.getMessage().substring(e.getMessage().indexOf("Key"));
		            }
		        }
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,msg,employeeId);
	    		
		   }  
    }

}
