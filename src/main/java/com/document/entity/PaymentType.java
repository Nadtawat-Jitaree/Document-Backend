package com.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="PAYMENT_TYPE")
public class PaymentType extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="NAME")
	private String name;
	
	@Column(name="STATUS")
	private String status;
	
}
