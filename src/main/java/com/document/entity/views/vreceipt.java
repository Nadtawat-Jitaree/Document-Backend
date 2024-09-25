package com.document.entity.views;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.document.entity.Company;


import lombok.Data;

@Data
@Entity
@Table(name = "VRECEIPT")
public class vreceipt implements Serializable {
	@Id
    private Long id;
	
	private String receipt_No;
	private Date receipt_Date;
	private Double total;
	private Double receipt_amt;
	private Double vat;
	private Double after_vat;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	private String inv_No;
	private Date inv_Date;
	  
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
