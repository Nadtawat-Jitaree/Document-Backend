package com.document.model;

import java.util.Date;
import java.util.List;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpEvalHeadRequest {
	private String reportId;
    private String prefix;
    private String name;
    private String surname;
    private String position;
    private Date evaDateFr;
    private Date evaDateTo;
    private String empEvaluationHeadID;
    private String department;
    private String evaluation;
    private Float totalScore;
    private Long empEvaluationHead;
    
    private List<Long> empEvaluationHeadList;
}
