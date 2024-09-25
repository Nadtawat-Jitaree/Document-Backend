package com.document.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;

import com.document.entity.Company;
import com.document.entity.InvDetail;
import com.document.entity.InvHead;
import com.document.entity.Quotation;
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
public class QuotationRequest {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
	private String qt_No;
	private Date qt_Date;
	private Double total;
	private Double qt_amt;
	private Double vat;
	private Double after_vat;
	private Double other_tax;
	private Double summary;
    private Company Company;
    private Long CompanyId;
	private String project;
	private String prs_Name;
	private String rec_Name;
	private String status;
	private String param;
	
	private String name;
	private Double num;
	private Double inv_amt;
	private Double discount;
	private Double pay;
    private Quotation quotation;
    private InvHead invHead;
    private Receipt receipt;
    private Long QuotationId;
    private Long InvHeadId;
    private Long ReceiptId;
	
	@ElementCollection
    private List<InvDetail> invDetail = new ArrayList<>();
}
