package com.document.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name="EMPLOYEE_APP_ROLE")
public class EmployeeAppRole extends BaseEntity{
	
	@Id
	@Column(name="ID")
	private Long id;

	//AppRole
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APP_ROLE_ID")
	private AppRole appRole;

	//Employee
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_ID")
	private Employee employee;

}
