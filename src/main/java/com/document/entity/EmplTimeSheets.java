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

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="EMPL_TIME_SHEETS")
public class EmplTimeSheets extends BaseEntity{
	private static final String MY_TIME_ZONE="Asia/Bangkok";
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
    @JsonFormat(pattern="dd-MM-yyyy",timezone = MY_TIME_ZONE)
	@Column(name="DONE_DATE")
	private Date doneDate;
	
	@Column(name="TIME_SHEET_STATUS")
	private String timeSheetStatus;
	
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE",
            referencedColumnName = "ID",
            nullable = false)
	private Employee employee;	
	
}
