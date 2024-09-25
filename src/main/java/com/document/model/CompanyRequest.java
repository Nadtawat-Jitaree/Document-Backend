package com.document.model;

import java.util.Date;

import com.document.entity.CompanyType;
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
public class CompanyRequest {
    private Long id;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private String name_th;
    private String name_en;
    private String address_th1;
    private String address_en1;
    private String address_th2;
    private String address_en2;
    private String name;
    private String tel;
    private String email;
    private String taxId;
    private String contact_type;
    private String status;
    private String id_type;
    private CompanyType company_type;
    private String contact_name;
    private String url;
    private Date reg_date;
}
