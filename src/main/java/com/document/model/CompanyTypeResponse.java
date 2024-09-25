package com.document.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyTypeResponse {
    private Long id;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private String name;

}
