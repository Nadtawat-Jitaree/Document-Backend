package com.document.utility;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ReportUtils {
	
	@SneakyThrows
    public static String getLogo() {
        try {
            String base64 = null;
            InputStream in = ReportUtils.class.getResourceAsStream("/images/" + "logo.png");
            byte[] bytes = IOUtils.toByteArray(in);
            byte[] encoded = Base64.getEncoder().encode(bytes);
            base64 = new String(encoded);
            return base64;
        }catch (Exception e){
            return null;
        }
    }

}
