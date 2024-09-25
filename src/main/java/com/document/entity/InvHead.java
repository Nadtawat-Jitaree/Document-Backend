package com.document.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="INV_HEAD")
public class InvHead extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="INV_NO")
	private String inv_No;
	
	@Column(name="INV_DATE")
	private Date inv_Date;

	@Column(name="TOTAL")
	private Double total;
	
	@Column(name="INV_AMT")
	private Double inv_amt;
	
	@Column(name="VAT")
	private Double vat;
	
	@Column(name="AFTER_VAT")
	private Double after_vat;
	
	@Column(name="OTHER_TAX")
	private Double other_tax;
	
	@Column(name="SUMMARY")
	private Double summary;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="COMPANY",nullable = false)
    private Company Company;
	
	@Column(name="PROJECT")
	private String project;
	
	@Column(name="RECEIVER_NAME")
	private String receiver_Name;
	
	@Column(name="SENDER")
	private String sender;
	
	@Column(name="PRS_NAME")
	private String prs_Name;
	
	@Column(name="REC_NAME")
	private String rec_Name;
	
	@Column(name="STATUS")
	private String status;
	
}
