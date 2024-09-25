package com.document.request;

import com.document.model.auth.TokenAuthorization;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthLoginResp implements Serializable {

    private TokenAuthorization authorization;
    private String token;

}
