package com.document.model;
import com.document.entity.Company;
import com.document.entity.InvDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationResponse {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
	private String qt_No;
	private Date qt_Date;
	private Double total;
	private Double qt_amt;
	private Double vat;
	private Double after_vat;
	private Double other_tax;
	private Double summary;
    private Company Company;
    private Long CompanyId;
	private String project;
	private String prs_Name;
	private String rec_Name;
	private String status;

	@ElementCollection
    private List<InvDetail> invDetail = new ArrayList<>();
}
