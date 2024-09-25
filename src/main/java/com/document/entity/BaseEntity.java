package com.document.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.document.utility.DateUtils;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity implements Serializable {

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate ;
	
	@Column(name="UPDATED_DATE")
	private Date updatedDate ;

	@PrePersist
	protected void onCreate() {
		createdDate = DateUtils.getNowDate();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = DateUtils.getNowDate();
	}
}
