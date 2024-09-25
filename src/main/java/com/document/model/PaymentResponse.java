package com.document.model;
import com.document.entity.PaymentType;
import com.document.entity.Receipt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {
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
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentType payment_type;;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Receipt receipt;
    private String status;
}
