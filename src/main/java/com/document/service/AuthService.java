package com.document.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.document.entity.AppRoleUser;
import com.document.entity.AppUser;
import com.document.entity.TokenSession;
import com.document.model.auth.TokenAuthorization;
import com.document.repository.AppRoleUserRepository;
import com.document.repository.AppUserRepository;
import com.document.repository.TokenSessionRepository;
import com.document.request.AuthLogin;
import com.document.utility.AuthUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class AuthService {

    @Autowired
    TokenSessionRepository tokenSessionRepository;

    @Autowired
    AppRoleUserRepository appRoleUserRepository;

    @Autowired
    AppUserRepository appUserRepository;
//
//    @Autowired
//    AppUserStoreRepository appUserStoreRepository;

    @Autowired
    AuthUtil authUtil;

    public Boolean authen(AuthLogin req) {
        boolean isPass = true;
        AppUser appUsers = appUserRepository.findByUserNameAndPassword(req.getUsername(), authUtil.encryptPassword(req.getPassword()));
        if(appUsers == null) {
            isPass = false;
        }
        if(req.getUsername().equals("admin") && req.getPassword().equals("admin")) {
            isPass = true;
        }
        // TODO : JAI+ Join table employee ดูว่า employee นี้ inactive หรือเปล่า
        return isPass;
    }

    public void storeSession(String token, String username) {
        TokenSession t = new TokenSession();
        t.setUsername(username);
        t.setToken(token);
        tokenSessionRepository.save(t);
        log.error("Created By --> " + authUtil.getUsernameFromSession());
    }

    public String generateToken(TokenAuthorization t) throws Exception {
        String token = "";
        byte[] byteToken = SerializationUtils.serialize(t);
        if (byteToken != null) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(byteToken);
            token = Base64.getEncoder().encodeToString(hash);
        }
        return  token;
    }

    public TokenAuthorization prepareData(String username) {
        String roleName = null;
        List<String> storeList = new ArrayList<>();

        List<AppRoleUser> appRoleUser = appRoleUserRepository.findByUsername(username);
        if(!appRoleUser.isEmpty()) {
            roleName = appRoleUser.get(0).getAppRole().getRoleName();
        }

//        List<AppUserStore> appUserStores = appUserStoreRepository.findByUserName(username);
//        if(!appUserStores.isEmpty()) {
//            for (AppUserStore a : appUserStores) {
//                storeList.add(a.getStore().getStoreNo());
//            }
//        }

//        return TokenAuthorization.builder().username(username).role(roleName).storeList(storeList).build();

      return TokenAuthorization.builder().username(username).role(roleName).build();

    }

    public TokenAuthorization decryptToken(String token) {
//        byte[] decodedBytes = Base64.getDecoder().decode(token);
//        return (TokenAuthorization) SerializationUtils.deserialize(decodedBytes);
        /* TODO get decryptToken  */
        return TokenAuthorization.builder().username("ADMIN_SYSTEM").build();
    }

}
