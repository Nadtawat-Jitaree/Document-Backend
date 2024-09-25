package com.document.model;


import java.util.Date;
import javax.persistence.Id;

import com.document.entity.Company;
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
public class ReceiptReport {

	@Id
    private Long id;
	private String reportId;
	private String receipt_No;
	private Date receipt_Date;
	private Double total;
	private Double receipt_amt;
	private Double vat;
	private Double after_vat;
    private Company company;
	private String project;
	private String payment_Name;
	private String rec_Name;
	private String status;
   
	private String name;
	private Double num;
	private Double inv_amt;
	private Double discount;
	private Double pay;
	
	private Long quotation;
	private Long invHead;
	private Long receipt;
	
    private String name_th;
    private String name_en;
    private String address_th1;
    private String address_en1;
    private String address_th2;
    private String address_en2;
    private String tel;
    private String email;
    private String taxId;
    private String contact_type;
    private String contact_name;

}
