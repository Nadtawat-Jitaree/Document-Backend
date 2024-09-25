package com.document.repository;

import com.document.entity.Receipt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

@Transactional
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    @Query("select t from Receipt t where t.receipt_No LIKE %:receipt_No% "   
			+ "and   (t.receipt_Date >= TO_DATE(:receipt_DateFr, 'yyyy-MM-dd') OR :receipt_DateFr = '') "
			+ "and  (t.receipt_Date <= TO_DATE(:receipt_DateTo, 'yyyy-MM-dd') OR :receipt_DateTo = '')  "
			+ "and  t.project LIKE %:project%  "
			+ "and  t.payment_Name LIKE %:payment_Name%  "
			+ "and  t.rec_Name LIKE %:rec_Name%  "
			+ "and  t.status LIKE %:status%  ")
    Page<Receipt> findAllByCriteria(String receipt_No,String receipt_DateFr,String receipt_DateTo,String project,String payment_Name,String rec_Name,String status,   Pageable pageable);
    
    @Query("select t from Receipt t where t.id=:id ")
    
    Receipt  findById(Long id);
    void deleteById(Long id);

}
