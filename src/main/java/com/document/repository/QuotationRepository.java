package com.document.repository;

import com.document.entity.Quotation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

@Transactional
public interface QuotationRepository extends JpaRepository<Quotation, Integer> {

    
    @Query("select t from Quotation t where t.qt_No LIKE %:qt_No% "   
    			+ "and   (t.qt_Date >= TO_DATE(:qt_DateFr, 'yyyy-MM-dd') OR :qt_DateFr = '') "
    			+ "and  (t.qt_Date <= TO_DATE(:qt_DateTo, 'yyyy-MM-dd') OR :qt_DateTo = '')  "
    			+ "and  t.project LIKE %:project%  "
    			+ "and  t.prs_Name LIKE %:prs_Name%  "
    			+ "and  t.rec_Name LIKE %:rec_Name%  "
    			+ "and  t.status LIKE %:status%  ")
    Page<Quotation> findAllByCriteria(String qt_No,String qt_DateFr,String qt_DateTo,String project,String prs_Name,String rec_Name,String status,  Pageable pageable);
    
    @Query("select t from Quotation t where t.id=:id ")
    
    Quotation  findById(Long id);
    void deleteById(Long id);


}
