package com.document.repository;

import com.document.entity.CompanyType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

@Transactional
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {

    @Query("select t from CompanyType t where t.name LIKE %:name% ")
    Page<CompanyType> findAllByCriteria(String name,  Pageable pageable);
    
    @Query("select t from CompanyType t where t.id=:id ")
    
    CompanyType  findById(Long id);
    void deleteById(Long id);

}
