package com.document.model;
import com.document.entity.CompanyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyResponse {
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
    private String contact_name;
    private String status;
    private String id_type;
    private CompanyType company_type;
    private String url;
    private String NameType;
    private Long CompanyTypeId;
    private Date reg_date;

}
