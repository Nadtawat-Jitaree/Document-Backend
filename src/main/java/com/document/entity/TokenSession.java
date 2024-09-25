package com.document.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "TOKEN_SESSION")
public class TokenSession {

    @Id
    @NotNull
    @Column(unique = true)
    private String username;

    private String token;

    private Date expiredAt;

}
