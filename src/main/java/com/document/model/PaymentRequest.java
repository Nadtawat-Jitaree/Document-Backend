package com.document.model;

import java.util.Date;

import com.document.entity.PaymentType;
import com.document.entity.Receipt;
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
public class PaymentRequest {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
    private Double total;
    private Double payment_amt;
    private Double vat;
    private Double summary;
    private String bank_name;
    private String bank_id;
    private Date bank_date;
    private PaymentType payment_type;
    private Receipt receipt;
    private String status;
}
