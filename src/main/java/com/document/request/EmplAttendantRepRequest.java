package com.document.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*(String employeeNo,String storeNo,String workDateFr,String workDateTo);*/
@JsonPropertyOrder({
    "employeeNo",
    "storeNo",
    "workDateFr",
    "workDateTo"    
})
public class EmplAttendantRepRequest {
	
	@JsonProperty("employeeNo") 
	private String employeeNo;
	
	@JsonProperty("storeNo") 
	private String storeNo;

	@JsonProperty("workDateFr") 
	private String workDateFr;
	
	@JsonProperty("workDateTo") 
	private String workDateTo;
	
	
	
}
