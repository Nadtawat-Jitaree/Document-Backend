package com.document.repository;

import com.document.entity.EmplAttendantRep;
import com.document.entity.EmplTimeAttendant;
import com.document.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmplAttendantRepRepository extends JpaRepository<EmplAttendantRep, Integer> {

	  @Query("select t from EmplAttendantRep t where t.employeeNo LIKE %:employeeNo%")
	   Page<EmplAttendantRep> findAllByCriteria(String employeeNo,   Pageable pageable);
	    
	    
    
    @Query("select t from EmplAttendantRep t ")
    Page<EmplAttendantRep> findAllPage(Pageable pageable);
    
    
  
    EmplTimeAttendant findById(Long id);
    

    @Query("select t from EmplAttendantRep t " +
            "WHERE t.employeeNo LIKE %:employeeNo% ")
    List<EmplAttendantRep> findByEmployeeNo(String employeeNo);
    
    @Query("select t from EmplAttendantRep t " +
            "WHERE t.employeeNo LIKE %:employeeNo% " +  
    		"AND TRUNC(t.workDate) = TO_DATE(:workDate, 'DD/MM/YYYY') ")
    EmplAttendantRep findByEmplDate(String employeeNo,String workDate);
    

    @Query("select t from EmplAttendantRep t " +
            "WHERE ( t.employeeNo LIKE %:employeeNo% OR :employeeNo is null )  " + 
    		"AND (:workDateFr is null OR :workDateTo is null OR DATE_TRUNC('Day',t.workDate) BETWEEN TO_DATE(:workDateFr, 'DD/MM/YYYY') " +
            "AND TO_DATE(:workDateTo, 'DD/MM/YYYY') ) " )
    Page<EmplAttendantRep> findByEmplPeriod(String employeeNo,String workDateFr,String workDateTo,Pageable pageable);
    
    
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
