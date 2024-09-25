package com.document.model;
import com.document.entity.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptResponse {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
	private String receipt_No;
	private Date receipt_Date;
	private Double total;
	private Double receipt_amt;
	private Double vat;
	private Double after_vat;
    private Company Company;
    private Long CompanyId;
	private String project;
	private String payment_Name;
	private String rec_Name;
	private String status;

}
