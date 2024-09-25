package com.document.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.entity.AppRole;
import com.document.entity.AppRoleUser;
import com.document.entity.AppUser;
import com.document.repository.AppRoleRepository;
import com.document.repository.AppRoleUserRepository;
import com.document.repository.AppUserRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.AppRoleService;
import com.document.service.AppRoleUserService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/roleUser")
public class AppRoleUserController {

    @Autowired
    AppRoleService appRoleService;
    
    @Autowired
    AppRoleUserService appRoleUserService;

    @Autowired
    AppRoleRepository appRoleRepository;
    
    @Autowired
    AppRoleUserRepository appRoleUserRepository;
    
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRoleUser(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "appRole.roleName") String sort,
            @RequestParam(required = false, defaultValue = "") String roleName,
            @RequestParam(required = false, defaultValue = "") String roleDesc,
            @RequestParam(required = false, defaultValue = "") String userName) {
        Page<AppRoleUser> list = appRoleUserService.findAllByCriteria(page, size, order, sort, roleName, roleDesc, userName);
        BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getRole(@RequestParam String userName) {
        BaseResponse response = BaseResponse.builder().data(appRoleUserRepository.findByUsername(userName)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @PutMapping("/{roleUserId}")
    public String updateParam(@PathVariable("roleUserId") Long roleUserId,@Valid @RequestBody AppRoleUser req) {
        AppRoleUser appRoleUserUpdate = appRoleUserRepository.findById(roleUserId);
        AppUser appUser = req.getAppUser();
        AppRole appRole = req.getAppRole();
        appRoleUserUpdate.setAppUser(appUser);
        appRoleUserUpdate.setAppRole(appRole);
        appRoleUserUpdate.setUpdatedBy(authUtil.getUsernameFromSession());
        appRoleUserRepository.save(appRoleUserUpdate);
        return "Update success";
    }

    @PostMapping
    public String insertParam(@Valid @RequestBody AppRoleUser appRoleUser) {
        appRoleUser.setCreatedBy(authUtil.getUsernameFromSession());
        appRoleUser.setCreatedDate(new Date());
        appRoleUserRepository.save(appRoleUser);
        return "Insert param success";
    }

    @DeleteMapping("/{roleUserId}")
    public String deleteParam(@PathVariable("roleUserId") Long roleUserId) {
        // TO DO : Confirm เมื่อ Role ถูก Assign ไปแล้ว จะลบออกไม่ได้
        appRoleUserRepository.deleteById(roleUserId);
        return "Delete param success";
    }

}
