package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.AppUser;
import com.document.entity.Employee;
import com.document.repository.AppUserRepository;
import com.document.repository.EmployeeRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.respond.ServerResponse;
import com.document.service.EmployeeService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;

import java.sql.SQLException;
import java.util.Date;
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;
    
  
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllEmployee(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "employeeNo") String sort,
            @RequestParam(required = false, defaultValue = "") String employeeNo,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String surName) {
        Page<Employee> list = employeeService.findAllByCriteria(page, size, order, sort, employeeNo, name, surName);
    	
    	BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
    	
    	return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam String employeeNo) {
        BaseResponse response = BaseResponse.builder().data(employeeRepository.findByEmployeeNo(employeeNo)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    
    @GetMapping("/allid")
    public ResponseEntity<Object> get(@RequestParam Long employeeId) {
        BaseResponse response = BaseResponse.builder().data(employeeRepository.findById(employeeId)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
   
    @PutMapping("/{employeeId}")
    public ServerResponse updateParam(@PathVariable(value = "employeeId") Long employeeId,@RequestBody Employee req) {
		 
    	System.out.println("Req:" + req.getId());
    	System.out.println("Req:" + req.getEmployeeNo());
    	
    	try {
			    //Get Store
    		    //Get AppUser
    		   /*		   
		    	Ou ou = new Ou();
		    	ou = req.getOu();
		    	System.out.println("OU:"+ ou.getId());
		    	ItemMainType itemMainTypeUpdate = itemMainTypeRepository.findById(itemMainTypeId);
		        itemMainTypeUpdate.setItemTypeCD(req.getItemTypeCD());
		        itemMainTypeUpdate.setItemTypeDesc(req.getItemTypeDesc());
		        itemMainTypeUpdate.setStatus(req.getStatus());
		        itemMainTypeUpdate.setOu(ou);
		        itemMainTypeUpdate.setUpdatedBy(authUtil.getUsernameFromSession());
		        itemMainTypeUpdate.setUpdatedDate(new Date());
		        itemMainTypeRepository.save(itemMainTypeUpdate);
	*/
    			
    			
    			System.out.println("AppUer:" +  req.getAppUser());
    			
    			Employee updEmployee = employeeRepository.findById(req.getId());
    			
		    	updEmployee.setUpdatedBy(authUtil.getUsernameFromSession());
		        updEmployee.setUpdatedDate(new Date());
		        updEmployee.setName(req.getName());
		        updEmployee.setSurName(req.getSurName());
		        updEmployee.setDepartment(req.getDepartment());
		        updEmployee.setPosition(req.getPosition());
		        updEmployee.setEmail(req.getEmail());
		        //Store
		    
		        updEmployee.setVerify(req.getVerify());
		        updEmployee.setPrefix(req.getPrefix());
		        updEmployee.setAppUser(req.getAppUser());
		        employeeRepository.save(updEmployee);
		        
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS,req);
		   } catch (Exception e) {
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
	    		
		   }
		        
    } 

    @PostMapping
    public ServerResponse insertParam(@Valid @RequestBody Employee req) {
		   try {
			   
//			    Optional<Ou> ou = ouRepository.findById(req.getOu().getId());
//			    System.out.println("OU"+ou.isPresent());
//			    req.setOu(ou.get());
		        req.setCreatedBy(authUtil.getUsernameFromSession());
		        req.setCreatedDate(new Date());
		      
			    //AppUser
			    AppUser appUser = appUserRepository.findById(req.getAppUser().getId());
			    req.setAppUser(appUser);
		        employeeRepository.save(req);
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,req);
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
