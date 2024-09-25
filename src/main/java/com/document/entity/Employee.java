package com.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="EMPLOYEE")
public class Employee extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="EMPLOYEE_NO")
	private String employeeNo;
	
	@Column(name="EMAIL")
	private String email;

	@Column(name="PREFIX")
	private String prefix;

	@Column(name="NAME")
	private String name;

	@Column(name="SURNAME")
	private String surName;
	
	@Column(name="POSITION")
	private String position;
	
	@Column(name="DEPARTMENT")
	private String department;	
	
	
	@Column(name="VERIFY")
	private String verify;	
	
	@ManyToOne
	@JoinColumn(name = "APP_USER",
            referencedColumnName = "ID",
            nullable = false)
	private AppUser appUser;	
	
	@Column(name="STATUS")
	private String status;
	
}
