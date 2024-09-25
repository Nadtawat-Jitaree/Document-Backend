package com.document.model.auth;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class TokenAuthorization implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private String role;
   

}
