package com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.EmplAttendantRep;
import com.document.repository.EmplAttendantRepRepository;

@Slf4j
@Service
public class EmplAttendantRepService {


    @Autowired
    EmplAttendantRepRepository emplAttendantRepRepository;

    public Page<EmplAttendantRep> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String employeeNo,  String workDateFr,String workDateTo){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        
        return emplAttendantRepRepository.findByEmplPeriod(employeeNo,  workDateFr, workDateTo, pageable);
    }
    
   
    
    
    public Page<EmplAttendantRep> findAllPage(Integer page, Integer size, Sort.Direction order, String sort){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return emplAttendantRepRepository.findAllPage(pageable);
    }
    
}
