package com.document.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.CompanyType;
import com.document.model.CompanyTypeRequest;
import com.document.model.CompanyTypeResponse;
import com.document.repository.CompanyTypeRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class CompanyTypeService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    CompanyTypeRepository companyTypeRepository;

    public Page<CompanyType> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String name){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return companyTypeRepository.findAllByCriteria( name, pageable);
    }
    
    @Transactional
    public List<CompanyTypeResponse> addFieldDesc(List<CompanyType> contents) {
        List<CompanyTypeResponse> response = new ArrayList<>();
        for(CompanyType content:contents){
        	CompanyTypeResponse companyResponse = CompanyTypeResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .name(content.getName())
                    .build();
            response.add(companyResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createCompanyType(CompanyTypeRequest payload) {
    	CompanyType companyType= new CompanyType();
    	companyType.setCreatedBy(authUtil.getUsernameFromSession());
    	companyType.setCreatedDate(new Date());
    	companyType.setName(payload.getName());
        
    	companyTypeRepository.save(companyType);
     }
    
}
