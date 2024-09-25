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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="COMPANY")
public class Company extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="NAME_TH")
	private String name_th;
	
	@Column(name="NAME_EN")
	private String name_en;
	
	@Column(name="ADDRESS_TH1")
	private String address_th1;
	
	@Column(name="ADDRESS_EN1")
	private String address_en1;	
	
	@Column(name="ADDRESS_TH2")
	private String address_th2;
	
	@Column(name="ADDRESS_EN2")
	private String address_en2;

	@Column(name="NAME")
	private String name;
	
	@Column(name="TEL")
	private String tel;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="TAXID")
	private String taxId;
	
	@Column(name="CONTACT_TYPE")
	private String contact_type;
	
	@Column(name="CONTACT_NAME")
	private String contact_name;
	
	@Temporal(TemporalType.DATE)
	@Column(name="REG_DATE")
	private Date reg_date;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="ID_TYPE")
	private String id_type;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JoinColumn(name = "COMPANY_TYPE",nullable = false)
	private CompanyType company_type;
	
	@Column(name="URL")
	private String url;
}
