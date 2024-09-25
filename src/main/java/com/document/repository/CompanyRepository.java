package com.document.repository;

import com.document.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select t from Company t where t.name LIKE %:name% and t.name_th LIKE %:name_th% and t.status LIKE %:status% and t.company_type.name LIKE %:company_type% ")
    Page<Company> findAllByCriteria(String company_type,String name, String name_th,String status,   Pageable pageable);
    
    @Query("select t from Company t where t.id=:id ")
    
    Company  findById(Long id);
    void deleteById(Long id);
    
    @Query("SELECT t FROM Company t WHERE LOWER(t.company_type.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Company> findById2(@Param("name") String name);


}
