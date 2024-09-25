package com.document.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.document.entity.AppUser;
import com.document.repository.AppUserRepository;

@Slf4j
@Service
public class AppUserService {

    @Autowired
    AppUserRepository repository;

    public Page<AppUser> findAllByCriteria(Integer page, Integer size, Sort.Direction order, String sort, String userName){
        Pageable pageable = PageRequest.of(page, size, new Sort(order, sort));
        return repository.findAllByCriteria(userName, pageable);
    }

}
