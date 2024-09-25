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
public class InvHeadRequest {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
	private String inv_No;
	private Date inv_Date;
	private Double total;
	private Double inv_amt;
	private Double vat;
	private Double after_vat;
	private Double withholdingTax;
	private Double summary;
    private Company Company;
	private String project;
	private String receiver_Name;
	private String sender;
	private String status;
	private String param;
	private String prs_Name;
	private String rec_Name;
	
	@ElementCollection
    private List<InvDetail> invDetail = new ArrayList<>();
}
