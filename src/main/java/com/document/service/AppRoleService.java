package com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.document.entity.AppRole;
import com.document.repository.AppRoleRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppRoleService {

    @Autowired
    AppRoleRepository appRoleRepository;

    public Page<AppRole> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String roleName, String roleDesc){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return appRoleRepository.findAllByCriteria(roleName, roleDesc, pageable);
    }

}
