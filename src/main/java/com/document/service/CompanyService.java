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

import com.document.entity.Company;
import com.document.entity.CompanyType;
import com.document.model.CompanyResponse;
import com.document.model.CompanyRequest;
import com.document.repository.CompanyRepository;
import com.document.repository.CompanyTypeRepository;
import com.document.utility.AuthUtil;

@Slf4j
@Service
public class CompanyService {

    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyTypeRepository companyTypeRepository;
    
    
    public Page<Company> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort,String company_type, String name, String name_th,String status){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return companyRepository.findAllByCriteria( company_type,name, name_th,status, pageable);
    }
    
    @Transactional
    public List<CompanyResponse> addFieldDesc(List<Company> contents) {
        List<CompanyResponse> response = new ArrayList<>();
        for(Company content:contents){
        	CompanyResponse companyResponse = CompanyResponse.builder()
                    .id(content.getId())
                    .createdBy(authUtil.getUsernameFromSession())
                    .updatedBy(authUtil.getUsernameFromSession())
                    .updatedDate(content.getUpdatedDate())
                    .createdDate(content.getCreatedDate())
                    .name_th(content.getName_th())
                    .name_en(content.getName_en())
                    .address_th1(content.getAddress_th1())
                    .address_en1(content.getAddress_en1())
                    .address_th2(content.getAddress_th2())
                    .address_en2(content.getAddress_en2())
                    .name(content.getName())
                    .reg_date(content.getReg_date())
                    .tel(content.getTel())
                    .email(content.getEmail())
                    .taxId(content.getTaxId())
                    .contact_type(content.getContact_type())
                    .contact_name(content.getContact_name())
                    .status(content.getStatus())
                    .id_type(content.getId_type())
                    .url(content.getUrl())
                    .build();
        	
            if (content.getCompany_type() != null){
            	CompanyType companyType = companyTypeRepository.findById(content.getCompany_type().getId());
            	companyResponse.setNameType(companyType.getName());
            	companyResponse.setCompanyTypeId(companyType.getId());

            }
        	
            response.add(companyResponse);
        }
        return  response;
    }
    
    @Transactional
    public void createCompany(CompanyRequest payload) {
    	Company company= new Company();
    	company.setCreatedBy(authUtil.getUsernameFromSession());
    	company.setCreatedDate(new Date());
    	company.setName_th(payload.getName_th());
    	company.setName_en(payload.getName_en());
    	company.setAddress_th1(payload.getAddress_th1());
    	company.setAddress_en1(payload.getAddress_en1());
    	company.setAddress_th2(payload.getAddress_th2());
    	company.setAddress_en2(payload.getAddress_en2());
    	company.setName(payload.getName());
    	company.setTel(payload.getTel());
    	company.setEmail(payload.getEmail());
    	company.setTaxId(payload.getTaxId());
    	company.setContact_type(payload.getContact_type());
    	company.setContact_name(payload.getContact_name());
    	company.setStatus(payload.getStatus());
    	company.setId_type(payload.getId_type());
    	company.setCompany_type(payload.getCompany_type());
    	company.setUrl(payload.getUrl());
    	company.setReg_date(payload.getReg_date());
    	
        companyRepository.save(company);
     }
    
	public void update(Long id, CompanyRequest req) {
		Company old = companyRepository.findById(id);
		old.setUpdatedBy(authUtil.getUsernameFromSession());
		old.setUpdatedDate(new Date());
		if(req.getName_th() == null) {
			old.setName_th(old.getName_th());
		}else {
			old.setName_th(req.getName_th());
		}
		if(req.getName_en() == null) {
			old.setName_en(old.getName_en());
		}else {
			old.setName_en(req.getName_en());
		}
		if(req.getAddress_th1() == null) {
			old.setAddress_th1(old.getAddress_th1());
		}else {
			old.setAddress_th1(req.getAddress_th1());
		}
		if(req.getAddress_en1() == null) {
			old.setAddress_en1(old.getAddress_en1());
		}else {
			old.setAddress_en1(req.getAddress_en1());
		}
		if(req.getAddress_th2() == null) {
			old.setAddress_th2(old.getAddress_th2());
		}else {
			old.setAddress_th2(req.getAddress_th2());
		}
		if(req.getAddress_en2() == null) {
			old.setAddress_en2(old.getAddress_en2());
		}else {
			old.setAddress_en2(req.getAddress_en2());
		}
		if(req.getName() == null) {
			old.setName(old.getName());
		}else {
			old.setName(req.getName());
		}
		if(req.getTel() == null) {
			old.setTel(old.getTel());
		}else {
			old.setTel(req.getTel());
		}
		if(req.getEmail() == null) {
			old.setEmail(old.getEmail());
		}else {
			old.setEmail(req.getEmail());
		}
		if(req.getTaxId() == null) {
			old.setTaxId(old.getTaxId());
		}else {
			old.setTaxId(req.getTaxId());
		}
		if(req.getContact_type() == null) {
			old.setContact_type(old.getContact_type());
		}else {
			old.setContact_type(req.getContact_type());
		}
		if(req.getContact_name() == null) {
			old.setContact_name(old.getContact_name());
		}else {
			old.setContact_name(req.getContact_name());
		}
		if(req.getStatus() == null) {
			old.setStatus(old.getStatus());
		}else {
			old.setStatus(req.getStatus());
		}
		if(req.getId_type() == null) {
			old.setId_type(old.getId_type());
		}else {
			old.setId_type(req.getId_type());
		}
		if(req.getCompany_type() == null) {
			old.setCompany_type(old.getCompany_type());
		}else {
			old.setCompany_type(req.getCompany_type());
		}
		if(req.getUrl() == null) {
			old.setUrl(old.getUrl());
		}else {
			old.setUrl(req.getUrl());
		}
		if(req.getReg_date() == null) {
			old.setReg_date(old.getReg_date());
		}else {
			old.setReg_date(req.getReg_date());
		}


		companyRepository.save(old);
	}
    
}
