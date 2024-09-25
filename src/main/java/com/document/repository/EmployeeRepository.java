package com.document.repository;

import com.document.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select t from Employee t where t.employeeNo LIKE %:employeeNo% and t.name LIKE %:name% and t.surName LIKE %:surName%  ")
    Page<Employee> findAllByCriteria(String employeeNo, String name, String surName,   Pageable pageable);
    
    
    @Query("select t from Employee t ")
    Page<Employee> findAllPage(Pageable pageable);
    
    

    @Query("select t from Employee t where t.id=:employeeId ")
    Employee  findById2(Long employeeId);
    
    Employee  findById(Long id);
    Employee findByEmployeeNo(String employeeNo);
    void deleteById(Long id);

}
