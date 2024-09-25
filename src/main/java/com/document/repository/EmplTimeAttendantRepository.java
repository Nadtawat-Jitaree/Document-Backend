package com.document.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.document.entity.EmplTimeAttendant;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmplTimeAttendantRepository extends JpaRepository<EmplTimeAttendant, Integer> {

	  @Query("select t from EmplTimeAttendant t where t.employeeNo LIKE %:employeeNo%")
	   Page<EmplTimeAttendant> findAllByCriteria(String employeeNo,   Pageable pageable);
	    
	    
    
    @Query("select t from EmplTimeAttendant t ")
    Page<EmplTimeAttendant> findAllPage(Pageable pageable);
    
    
  
    EmplTimeAttendant findById(Long id);
    

    @Query("select t from EmplTimeAttendant t " +
            "WHERE t.employeeNo LIKE %:employeeNo% ")
    List<EmplTimeAttendant> findByEmployeeNo(String employeeNo);
    
    @Query("select t from EmplTimeAttendant t " +
            "WHERE t.employeeNo LIKE %:employeeNo% " + 
    		"AND TRUNC(t.dateOn) = TO_DATE(:dateOn, 'DD/MM/YYYY')")
    EmplTimeAttendant findByEmplDate(String employeeNo,String dateOn);
    
    
    @Query("select t from EmplTimeAttendant t " +
            "WHERE t.employeeNo LIKE %:employeeNo% " +
    		"AND t.dateOn = ( " + 
            " select max(t2.dateOn) from EmplTimeAttendant t2 "  +
    		"WHERE t2.employeeNo = t.employeeNo " +
            "GROUP BY t2.employeeNo" +
            ")  " )
    List<EmplTimeAttendant> findTimeByEmployeeNo(String employeeNo);
    
    
    void deleteById(Long id);

}
