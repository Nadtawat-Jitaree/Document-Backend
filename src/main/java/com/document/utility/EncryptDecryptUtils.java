package com.document.utility;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
public class EncryptDecryptUtils {

    public String encryptPassword(String originalInput){
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        return Base64.getEncoder().encodeToString(encodedString.getBytes());
    }

    public String decryptPassword(String originalInput){
        byte[] decodedBytes = Base64.getDecoder().decode(originalInput);
        String decodedString = new String(decodedBytes);
        return new String(Base64.getDecoder().decode(decodedString));
    }

}
