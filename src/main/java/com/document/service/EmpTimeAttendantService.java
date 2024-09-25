package com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.EmplTimeAttendant;
import com.document.repository.EmplTimeAttendantRepository;

@Slf4j
@Service
public class EmpTimeAttendantService {


    @Autowired
    EmplTimeAttendantRepository emplTimeAttendantRepository;

    public Page<EmplTimeAttendant> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String employeeNo){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return emplTimeAttendantRepository.findAllByCriteria(employeeNo, pageable);
       }
    
    public Page<EmplTimeAttendant> findAllPage(Integer page, Integer size, Sort.Direction order, String sort){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return emplTimeAttendantRepository.findAllPage(pageable);
    }
    
}
