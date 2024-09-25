package com.document.entity;




import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "V_EMPLOYEE_TIME")
public class EmplAttendantRep implements Serializable {
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="EMPL_ID")
	private Long emplId;
	
	@Column(name="EMPLOYEE_NO")
	private String employeeNo;
	
	@Column(name="EMPLNAME")
	private String emplName;
	
	
	@Column(name="WORKDATE")
	private Date workDate;
	
	@Column(name="TIME_ON")
	private Date timeOn;
	
	@Column(name="TIME_OFF")
	private Date timeOff;
	
	@Column(name="WORKHOUR")
    private String workHour;
   
}
