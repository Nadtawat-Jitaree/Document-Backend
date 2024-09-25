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
@Table(name="QUATATION")
public class Quotation extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="QT_NO")
	private String qt_No;
	
	@Column(name="QT_DATE")
	private Date qt_Date;

	@Column(name="TOTAL")
	private Double total;
	
	@Column(name="QT_AMT")
	private Double qt_amt;
	
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
	
	@Column(name="PRS_NAME")
	private String prs_Name;
	
	@Column(name="REC_NAME")
	private String rec_Name;
	
	@Column(name="STATUS")
	private String status;
	
}
