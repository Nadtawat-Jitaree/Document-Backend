package com.document.model;
import com.document.entity.InvHead;
import com.document.entity.Quotation;
import com.document.entity.Receipt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvDetailResponse {
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Long id;
	private String name;
	private Double num;
	private Double inv_amt;
	private Double discount;
	private Double pay;
    private Quotation quotation;
    private InvHead invHead;
    private Receipt receipt;
    private Long QuotationId;
    private Long InvHeadId;
    private Long ReceiptId;

}
