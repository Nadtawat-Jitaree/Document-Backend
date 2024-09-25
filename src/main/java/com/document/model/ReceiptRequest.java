package com.document.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;

import com.document.entity.Company;
import com.document.entity.InvDetail;
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
public class ReceiptRequest {
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
	private String param;
	
    private Long payment_id;
	private Double payment_amt;
	private String bank_name;
	private String bank_id;
	private Date bank_date;
	private Long payment_type;
	
	@ElementCollection
    private List<InvDetail> invDetail = new ArrayList<>();
	
}
