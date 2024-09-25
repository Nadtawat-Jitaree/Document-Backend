package com.document.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="EMPL_TIME_SHEETS_DETAIL")
public class EmplTimeSheetsDetail extends BaseEntity{
	private static final String MY_TIME_ZONE="Asia/Bangkok";
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "EMPLTIMESHEETS",
	referencedColumnName = "ID",
	nullable = false)
	private EmplTimeSheets emplTimeSheets;
	
	@Column(name="SEQ")
	private Integer seq;
	
	@Column(name="JOB_DETAILS")
	private String jobDetails;
	
}
