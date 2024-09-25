package com.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="INV_DETAIL")
public class InvDetail extends BaseEntity{

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="NAME")
	private String name;
	
	@Column(name="NUM")
	private Double num;

	@Column(name="INV_AMT")
	private Double Inv_amt;
	
	@Column(name="DISCOUNT")
	private Double discount;
	
	@Column(name="PAY")
	private Double pay;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="QUOTATION", nullable = true)
    private Quotation quotation;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="INV_HEAD", nullable = true)
    private InvHead invHead;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name="RECEIPT", nullable = true)
    private Receipt receipt;
	
}
