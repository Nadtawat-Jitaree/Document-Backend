package com.document.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="EMPL_TIME_ATTENDANT")
public class EmplTimeAttendant extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="EMPLOYEE_NO")
	private String employeeNo;
	
	@Column(name="DateOn")
	private Date dateOn;
	
	@Column(name="TIME_ON")
	private LocalDateTime timeOn;
	
	@Column(name="TIME_OFF")
	private LocalDateTime timeOff;
	
	@Column(name="TIME_ZONE")
    private ZoneId timeZone;
	
	
	/*private LocalDateTime dateTime;

    @NotNull
    private ZoneId timeZone;
        ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), scheduleEmailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
*/
	
}
