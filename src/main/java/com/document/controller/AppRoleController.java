package com.document.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.document.entity.AppRole;
import com.document.repository.AppRoleRepository;
import com.document.respond.BasePagingResponse;
import com.document.respond.BaseResponse;
import com.document.service.AppRoleService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/role")
public class AppRoleController {

    @Autowired
    AppRoleService appRoleService;

    @Autowired
    AppRoleRepository appRoleRepository;

    @Autowired
    AuthUtil authUtil;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRole(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "roleName") String sort,
            @RequestParam(required = false, defaultValue = "") String roleName,
            @RequestParam(required = false, defaultValue = "") String roleDesc) {
        Page<AppRole> list = appRoleService.findAllByCriteria(page, size, order, sort, roleName, roleDesc);
        BasePagingResponse response = BasePagingResponse.builder().data(list.getContent()).page(list.getNumber()).totalSize(list.getTotalElements()).totalPage(list.getTotalPages()).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getRole(@RequestParam String roleName) {
        BaseResponse response = BaseResponse.builder().data(appRoleRepository.findByRoleName(roleName)).build();
        return new ResponseEntity<Object>(response, null, HttpStatus.OK);
    }

    @PutMapping("/{roleId}")
    public String updateParam(@PathVariable("roleId") Long roleId,@Valid @RequestBody AppRole req) {
        AppRole appRoleUpdate = appRoleRepository.findById(roleId);
        appRoleUpdate.setRoleName(req.getRoleName());
        appRoleUpdate.setRoleDesc(req.getRoleDesc());
        appRoleUpdate.setUpdatedBy(authUtil.getUsernameFromSession());
        appRoleRepository.save(appRoleUpdate);
        return "Update success";
    }

    @PostMapping
    public String insertParam(@Valid @RequestBody AppRole appRole) {
        appRole.setCreatedBy(authUtil.getUsernameFromSession());
        appRoleRepository.save(appRole);
        return "Insert param success";
    }

    @DeleteMapping("/{roleId}")
    public String deleteParam(@PathVariable("roleId") Long roleId) {
        // TO DO : Confirm เมื่อ Role ถูก Assign ไปแล้ว จะลบออกไม่ได้
        appRoleRepository.deleteById(roleId);
        return "Delete param success";
    }

}
