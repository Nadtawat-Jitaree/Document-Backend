package com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.AbsenceRequest;
import com.document.entity.Employee;
import com.document.repository.AbsenceRequestRepository;
import com.document.repository.EmployeeRepository;

@Slf4j
@Service
public class AbsenceRequestService {


    @Autowired
    AbsenceRequestRepository absenceRequestRepository;

    public Page<AbsenceRequest> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String employeeNo, String name, String surName){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return absenceRequestRepository.findAllByCriteria(employeeNo, name, surName, pageable);
    }
    
    public Page<AbsenceRequest> findAllPage(Integer page, Integer size, Sort.Direction order, String sort){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return absenceRequestRepository.findAllPage(pageable);
    }
    
}
