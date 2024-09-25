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

import com.document.entity.Param;
import com.document.model.ParamRequest;
import com.document.model.ParamResponse;
import com.document.repository.ParamRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class ParamService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    ParamRepository paramRepository;
    
    public Page<Param> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String paramCode,String paramType){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return paramRepository.findAllByCriteria( paramCode,paramType, pageable);
    }
    
    @Transactional
    public List<ParamResponse> addFieldDesc(List<Param> contents) {
        List<ParamResponse> response = new ArrayList<>();
        for(Param content:contents){
        	ParamResponse paramResponse = ParamResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .paramCode(content.getParamCode())
                    .paramType(content.getParamType())
                    .build();
        	
            response.add(paramResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createParam(ParamRequest payload) {
    	Param param= new Param();
    	param.setCreatedBy(authUtil.getUsernameFromSession());
    	param.setCreatedDate(new Date());
    	param.setParamCode(payload.getParamCode());
    	param.setParamType(payload.getParamType());
    	
    	paramRepository.save(param);
     }
    
    @Transactional
	public void update(Long id, ParamRequest req) {
		Param old = paramRepository.findById(id);
		old.setCreatedBy(old.getCreatedBy());
		old.setCreatedDate(old.getCreatedDate());
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		old.setParamCode(req.getParamCode());
		old.setParamType(req.getParamType());
		paramRepository.save(old);
	}
    
}
