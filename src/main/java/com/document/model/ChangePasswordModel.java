package com.document.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordModel {

    @NotBlank
    private String password;

}
