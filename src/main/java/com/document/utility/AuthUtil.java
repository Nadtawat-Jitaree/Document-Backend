package com.document.utility;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.document.constant.HeaderConst;
import com.document.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Component
public class AuthUtil {

    @Autowired
    AuthService authService;

    public String getTokenFromHeaderRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getHeader(HeaderConst.Authorization);
    }

    public String getUsernameFromSession() {
        /* TODO get username from token */
        return authService.decryptToken(getTokenFromHeaderRequest()).getUsername();
    }

    public String encryptPassword(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String decryptPassword(String password){
        return new String(Base64.getDecoder().decode(password));
    }

}
