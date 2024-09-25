package com.document.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;

import com.document.constant.ResponseConst;
import com.document.entity.AppUser;
import com.document.model.auth.TokenAuthorization;
import com.document.repository.AppUserRepository;
import com.document.request.AuthChangePassword;
import com.document.request.AuthLogin;
import com.document.request.AuthLoginResp;
import com.document.respond.BaseResponse;
import com.document.service.AuthService;
import com.document.utility.AuthUtil;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService service;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AuthUtil authUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthLogin req) throws Exception {
        if(service.authen(req)) {
            // pass
            TokenAuthorization authorization = service.prepareData(req.getUsername());
            String token = service.generateToken(authorization);
            service.storeSession(token, req.getUsername());
            AuthLoginResp authLoginResp = AuthLoginResp.builder().authorization(authorization).token(token).build();
            BaseResponse response = BaseResponse.builder().responseCode(ResponseConst.RESPONSE_SUCCESS).data(authLoginResp).build();
            return new ResponseEntity<Object>(response, null, HttpStatus.OK);
        } else {
            // not pass
            BaseResponse response = BaseResponse.builder().responseCode(ResponseConst.RESPONSE_FAIL).responseDesc(ResponseConst.DESC_INVALID_USERNAME_PASSWORD).build();
            return new ResponseEntity<Object>(response, null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/password/change")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody AuthChangePassword req) throws Exception {
        AuthLogin authLogin = AuthLogin.builder().username(req.getUsername()).password(req.getOldPassword()).build();
        if(service.authen(authLogin)) {
            // pass
            AppUser appUser = appUserRepository.findByUserName(req.getUsername());
            appUser.setPassword(authUtil.encryptPassword(req.getNewPassword()));
            appUserRepository.save(appUser);
            BaseResponse response = BaseResponse.builder().responseCode(ResponseConst.RESPONSE_SUCCESS).data(appUser).build();
            return new ResponseEntity<Object>(response, null, HttpStatus.OK);
        } else {
            // not pass
            BaseResponse response = BaseResponse.builder().responseCode(ResponseConst.RESPONSE_FAIL).responseDesc(ResponseConst.DESC_INVALID_USERNAME_PASSWORD).build();
            return new ResponseEntity<Object>(response, null, HttpStatus.UNAUTHORIZED);
        }
    }

}
