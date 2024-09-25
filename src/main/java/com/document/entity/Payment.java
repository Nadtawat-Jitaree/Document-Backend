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
@Table(name="PAYMENT")
public class Payment extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="TOTAL")
	private Double total;
	
	@Column(name="PAYMENT_AMT")
	private Double payment_amt;
	
	@Column(name="VAT")
	private Double vat;
	
	@Column(name="SUMMARY")
	private Double summary;
	
	@Column(name="BANK_NAME")
	private String bank_name;
	
	@Column(name="BANK_ID")
	private String bank_id;
	
	@Column(name="BANK_DATE")
	private Date bank_date;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="PAYMENT_TYPE", nullable = true)
    private PaymentType payment_type;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="RECEIPT", nullable = true)
    private Receipt receipt;
    
	@Column(name="STATUS")
	private String status;
	
}
