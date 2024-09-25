package com.document.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="ABSENCE_REQUEST")
public class AbsenceRequest extends BaseEntity{
	private static final String MY_TIME_ZONE="Asia/Bangkok";
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="ABS_TYPE")
	private String absType;
	
	@Column(name="ABS_DETAIL")
	private String absDetail;


    @JsonFormat(pattern="dd-MM-yyyy",timezone = MY_TIME_ZONE)
	@Column(name="ABS_START")
	private Date absStart;

    
	@JsonFormat(pattern="dd-MM-yyyy",timezone = MY_TIME_ZONE)@Column(name="ABS_END")
	private Date absEnd;
	
	@Column(name="ABS_DAYS")
	private Double absDays;

	@Column(name="STATUS")
	private String absStatus;
	
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE",
            referencedColumnName = "ID",
            nullable = false)
	private Employee employee;	
	
}
