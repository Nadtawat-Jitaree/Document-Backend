package com.document.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthChangePassword {

    private String username;
    private String oldPassword;
    private String newPassword;

}
