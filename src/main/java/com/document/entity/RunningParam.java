package com.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.document.entity.RunningParam;

import lombok.Data;
@Data
@Entity
public class RunningParam {
    @Id
    @Column(unique = true)
    private String paramCode;

    private Integer paramValue;
}
