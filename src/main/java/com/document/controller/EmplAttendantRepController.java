package com.document.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.entity.EmplAttendantRep;
import com.document.repository.EmplAttendantRepRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.EmplAttendantRepService;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/EmplAttendantRep")
public class EmplAttendantRepController {

    @Autowired
    EmplAttendantRepService emplAttendantRepService;

    @Autowired
    EmplAttendantRepRepository emplAttendantRepRepository;
    
    @GetMapping("/all")
    public ResponseEntity<Object> getAllEmplAttendant(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "employeeNo") String sort,
            @RequestParam(required = false, defaultValue = "") String employeeNo,
            @RequestParam(required = false, defaultValue = "") String storeNo,
            @RequestParam(required = false, defaultValue = "") String workDateFr,
            @RequestParam(required = false, defaultValue = "") String workDateTo) {
        Page<EmplAttendantRep> list = emplAttendantRepService.findAllPage(page, size, order, sort);
    	
    	BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
    	
    	return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
    

    @GetMapping("/search")
    public ResponseEntity<Object> getAllEmplCriteria(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "employeeNo") String sort,
            @RequestParam(required = false, defaultValue = "") String employeeNo,
            @RequestParam(required = false, defaultValue = "") String workDateFr,
            @RequestParam(required = false, defaultValue = "") String workDateTo) {
        Page<EmplAttendantRep> list = emplAttendantRepService.findAllByCriteria(page, size, order, sort, employeeNo,  workDateFr, workDateTo);
    	
    
    	BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
    	
    	return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam String employeeNo) {
        BaseResponse response = BaseResponse.builder().data(emplAttendantRepRepository.findByEmployeeNo(employeeNo)).responseCode("200").build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }
   
    
}
