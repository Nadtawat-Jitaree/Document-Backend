package com.document.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.document.entity.AppRoleUser;
import com.document.entity.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Data
@Getter
@Setter
public class CreateEmplAbsence {

	@NotBlank
    private String absType;
    
    @NotBlank
    private String absDetail;
    
    @NotNull
    private Double absDays;
    
   
    @JsonFormat(pattern = "dd-MM-yyyy")
	@NotNull
	private String absStart;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
	@NotNull
	private String absEnd;
    
    @NotBlank
    private String status;
        
    private Employee employee;

}
