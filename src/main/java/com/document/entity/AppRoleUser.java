package com.document.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Setter
@Getter
@Entity
@Table(name="APP_ROLE_USER")
public class AppRoleUser extends BaseEntity{
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	//AppRole
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JoinColumn(name="APP_ROLE",
			referencedColumnName = "ID",
            nullable = false)
	private AppRole appRole;

	//User
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JoinColumn(name="APP_USER",
			referencedColumnName = "ID",
            nullable = false)
	private AppUser appUser;

}
