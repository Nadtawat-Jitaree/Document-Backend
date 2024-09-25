package com.document.repository;

import com.document.entity.AbsenceRequest;
import com.document.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Integer> {

    @Query("select t from AbsenceRequest t where t.employee.employeeNo LIKE %:employeeNo% and t.employee.name LIKE %:name% and t.employee.surName LIKE %:surName%  ")
    Page<AbsenceRequest> findAllByCriteria(String employeeNo, String name, String surName,   Pageable pageable);
    
    
    @Query("select t from AbsenceRequest t ")
    Page<AbsenceRequest> findAllPage(Pageable pageable);
    
    

    @Query("select t from AbsenceRequest t where t.id=:requestId ")
    AbsenceRequest  findById2(Long requestId);
    
    AbsenceRequest  findById(Long id);
    
    void deleteById(Long id);

}
