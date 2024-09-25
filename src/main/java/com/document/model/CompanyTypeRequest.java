package com.document.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyTypeRequest {
    private Long id;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private String name;
}
