package com.document.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.document.entity.Param;

@Transactional
public interface ParamRepository extends JpaRepository<Param, Integer>{
	
	@Query("select t from Param t where t.paramCode LIKE %:paramCode% AND t.paramType LIKE %:paramType%")
		Page<Param> findAllByCriteria(String paramCode,String paramType, Pageable pageable);
	
    @Query("select t from Param t where t.id=:id ")
	Param  findById(Long id);
    
    void deleteById(Long id);
    
    
}
