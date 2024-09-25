package com.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.document.entity.RunningParam;


public interface RunningParamRepository extends JpaRepository<RunningParam, String>{
	
	RunningParam findByParamCode(String paramCode);
}
