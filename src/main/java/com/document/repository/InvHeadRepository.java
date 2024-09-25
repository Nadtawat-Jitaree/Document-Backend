package com.document.repository;

import com.document.entity.InvHead;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

@Transactional
public interface InvHeadRepository extends JpaRepository<InvHead, Integer> {

    @Query("select t from InvHead t where t.inv_No LIKE %:inv_No% "   
			+ "and   (t.inv_Date >= TO_DATE(:inv_DateFr, 'yyyy-MM-dd') OR :inv_DateFr = '') "
			+ "and  (t.inv_Date <= TO_DATE(:inv_DateTo, 'yyyy-MM-dd') OR :inv_DateTo = '')  "
			+ "and t.project LIKE %:project% "
			+ "and t.sender LIKE %:sender% "
			+ "and t.receiver_Name LIKE %:receiver_Name% "
			+ "and  t.status LIKE %:status%  ")
    Page<InvHead> findAllByCriteria(String inv_No,String inv_DateFr,String inv_DateTo,String project,String sender,String receiver_Name,String status,   Pageable pageable);
    
    @Query("select t from InvHead t where t.id=:id ")
    
    InvHead  findById(Long id);
    void deleteById(Long id);

}
