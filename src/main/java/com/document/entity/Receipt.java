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
@Table(name="RECEIPT")
public class Receipt extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="RECEIPT_NO")
	private String receipt_No;
	
	@Column(name="RECEIPT_DATE")
	private Date receipt_Date;

	@Column(name="TOTAL")
	private Double total;
	
	@Column(name="RECEIPT_AMT")
	private Double receipt_amt;
	
	@Column(name="VAT")
	private Double vat;
	
	@Column(name="AFTER_VAT")
	private Double after_vat;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="COMPANY",nullable = false)
    private Company Company;
	
	@Column(name="PROJECT")
	private String project;
	
	@Column(name="PAYMENT_NAME")
	private String payment_Name;
	
	@Column(name="REC_NAME")
	private String rec_Name;
	
	@Column(name="STATUS")
	private String status;
	
}
