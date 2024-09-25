package com.document.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name="APP_USER")
public class AppUser extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@NotBlank
	@Column(name="USER_NAME", unique = true)
	private String userName;

	@NotBlank
	@Column(name="PASSWORD")
	private String password;

	@JsonIgnore
	public String getPassword() {
		return password;
	}

}
