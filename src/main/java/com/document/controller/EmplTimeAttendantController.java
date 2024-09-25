package com.document.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.EmplTimeAttendant;
import com.document.repository.EmplTimeAttendantRepository;
import com.document.repository.EmployeeRepository;
import com.document.request.EmployeeTimePunchIn;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.respond.ServerResponse;
import com.document.service.EmpTimeAttendantService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/emplTimeAttendant")
public class EmplTimeAttendantController {

    @Autowired
    EmplTimeAttendantRepository emplTimeAttendantRepository;
    
    @Autowired
    EmpTimeAttendantService emplTimeAttendantService;
    
    @Autowired
    EmployeeRepository employeeRepository;
    
    
    @Autowired
    AuthUtil authUtil;
    
    

    @GetMapping("/all")
    public ResponseEntity<Object> getAllEmplTimeAttendant(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "employeeNo") String sort,
            @RequestParam(required = false, defaultValue = "") String employeeNo) {
        Page<EmplTimeAttendant> list = emplTimeAttendantService.findAllByCriteria(page, size, order, sort, employeeNo);
    	
    	BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
    	
    	return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam String employeeNo) {
        BaseResponse response = BaseResponse.builder().data(emplTimeAttendantRepository.findByEmployeeNo(employeeNo)).responseCode("200").build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
   
    @PutMapping("/{emplTimeAttendantId}")
    public ServerResponse updateParam(@PathVariable(value = "emplTimeAttendantId") Long emplTimeAttendantId,@RequestBody EmplTimeAttendant req) {
		 
    	System.out.println("Req:" + req.getId());
    	System.out.println("Req:" + req.getEmployeeNo());
    	
    	try {
			    EmplTimeAttendant emplTime = emplTimeAttendantRepository.findById(req.getId());
			    //Time Stamp Off only
			    if (emplTime.getTimeOff()==null ) {
			    	emplTime.setTimeOff(req.getTimeOff());
			        emplTime.setUpdatedBy(authUtil.getUsernameFromSession());
		            emplTime.setUpdatedDate(new Date());
		            emplTimeAttendantRepository.save(emplTime);
		            return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS,emplTime);		  		            
			    }
			    else 
			    	  return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
		    	
			    		
			   } catch (Exception e) {
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
	    		
		   }
		        
    } 

    @PostMapping
    public ServerResponse insertParam(@Valid @RequestBody EmplTimeAttendant req) {
		   try {
			    req.setTimeOff(null);
		        emplTimeAttendantRepository.save(req);
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,req);
		   } catch (Exception e) {
			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
	    		
		   }    
	}
    
    @PostMapping(path="/clockIn")
    public ServerResponse clockIn(@Valid @RequestBody EmployeeTimePunchIn req) {
        System.out.println("ReqEMP:" + req.getEmployeeNo());
    	   List<EmplTimeAttendant> emplTimes = emplTimeAttendantRepository.findTimeByEmployeeNo(req.getEmployeeNo());
    	   //Found Data
    	   System.out.println("Size:" + emplTimes.size());
     	   //Check Date On
		   SimpleDateFormat formatDateOn = new SimpleDateFormat("ddMMyyyy");
		   Date workDate = new Date();
    	  
    	   if (emplTimes.size() != 0 ) {
    		   
//    		   try {
    		       //Time Off
    	           if ( formatDateOn.format(workDate).equals(formatDateOn.format(emplTimes.get(0).getDateOn())) ) {  //Time off
    		   	     //Check Date
    	        	   System.out.println("Date:Req" + formatDateOn.format(workDate) + ":DB1" + formatDateOn.format(emplTimes.get(0).getDateOn()) +":" + emplTimes.get(0).getTimeOff());
    	              	
      	             //Check Time Off
    	        	    if (emplTimes.get(0).getTimeOff()==null) { //Update
    	        	    	 EmplTimeAttendant updEmplTimeOff = emplTimeAttendantRepository.findById(emplTimes.get(0).getId());
    	        	    	 ZoneId emplZone = ZoneId.of("Asia/Bangkok");
    	        	    	 updEmplTimeOff.setTimeZone(emplZone);
    	        	    	 updEmplTimeOff.setTimeOff(LocalDateTime.now());
    	        	    	 updEmplTimeOff.setUpdatedDate(new Date());
    	        	    	 updEmplTimeOff.setUpdatedBy(authUtil.getUsernameFromSession());
    	        	    	 emplTimeAttendantRepository.save(updEmplTimeOff);
    	        	    	 return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_UPDATE_SUCCESS,updEmplTimeOff);
    	        	    } else {
    	        	    	//No Update 
    	        	    	return ServerResponse.getServerResponse(ResponseConst.METHOD_NOT_ALLOW,"Clock already off!",req);
    			    		
    	        	    }
    	        	    	
    	        	    
    	        	  
    	           } else {
                     //Create New WorkDate Time On
    	        	   System.out.println("Date:Req" + formatDateOn.format(workDate) + ":DB2" + formatDateOn.format(emplTimes.get(0).getDateOn()) +":" + emplTimes.get(0).getTimeOff());
      	             	        EmplTimeAttendant  emplTimeOn = 	new EmplTimeAttendant();
      	             	        emplTimeOn.setCreatedBy(authUtil.getUsernameFromSession());
      	             	        emplTimeOn.setCreatedDate( Date.from(ZonedDateTime.now().toInstant()));
      	             	        emplTimeOn.setDateOn(workDate);
     	             	        ZoneId emplZone = ZoneId.of("Asia/Bangkok");
     	             	        emplTimeOn.setTimeZone(emplZone);
      	             	        emplTimeOn.setEmployeeNo(req.getEmployeeNo());
      	             	        emplTimeOn.setTimeOn(LocalDateTime.now());
//      	             	        emplTimeOn.setTimeOn(emplZone.);
      	             	        emplTimeOn.setTimeOff(null);
      	             	        
						        emplTimeAttendantRepository.save(emplTimeOn);
//						        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,req);
						        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,emplTimeOn);
							       						       
    	           }
//    		   } catch (Exception e) {
//    			   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,ResponseConst.RESPONSE_MSG_FAIL,req);
//   	    		
//    		   }    
         } else { // Not Found Time   Time On
        	  System.out.println("New Punch In");
	       	    EmplTimeAttendant  newEmplTimeOn = 	new EmplTimeAttendant();
	  	        newEmplTimeOn.setCreatedBy(authUtil.getUsernameFromSession());
	  	        newEmplTimeOn.setCreatedDate(new Date());
	  	        newEmplTimeOn.setDateOn(workDate);
	  	        newEmplTimeOn.setEmployeeNo(req.getEmployeeNo());
	  	        newEmplTimeOn.setTimeOn(LocalDateTime.now());
	  	        newEmplTimeOn.setTimeOff(null);
	  	        ZoneId emplZone = ZoneId.of("Asia/Bangkok");
	  	        newEmplTimeOn.setTimeZone(emplZone);
	  
		        emplTimeAttendantRepository.save(newEmplTimeOn);
		        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_CREATE_SUCCESS,newEmplTimeOn);
		  
  	     }
    }
    	   
    	   @DeleteMapping("/{emplTimeAttendId}")
    	    public ServerResponse deleteParam(@PathVariable(value = "emplTimeAttendId") Long emplTimeAttendId) {
    	           try {
    			        emplTimeAttendantRepository.deleteById(emplTimeAttendId);
    			        return ServerResponse.getServerResponse(ResponseConst.SUCCESS,ResponseConst.RESPONSE_MSG_DELETE_SUCCESS,emplTimeAttendId);
    			   } catch (Exception ex) {
//    				   Optional<Ou> ou = ouRepository.findById(req.getOu().getId());
//    				    System.out.println("OU"+ou.isPresent());
    				   String msg = ex.getMessage();
    			        if (ex.getCause().getCause() instanceof SQLException) {
    			            SQLException e = (SQLException) ex.getCause().getCause();

    			            if (e.getMessage().contains("Key")) {
    			                msg = e.getMessage().substring(e.getMessage().indexOf("Key"));
    			            }
    			        }
    				   return ServerResponse.getServerResponse(ResponseConst.BAD_REQUEST,msg,emplTimeAttendId);
    		    		
    			   }  
    	    }
		
	

}
