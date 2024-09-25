package com.document.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Entity
@Table(name="APP_ROLE")
public class AppRole extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@NotBlank
	@Column(name="ROLE_NAME", unique = true)
	private String roleName;

	@NotBlank
	@Column(name="ROLE_DESC")
	private String roleDesc;

}
