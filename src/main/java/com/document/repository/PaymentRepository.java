package com.document.repository;
import com.document.entity.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	@Query("select t from Payment t where (t.receipt.id = :receipt OR :receipt = 0)")
	Page<Payment> findAllByCriteria(Long receipt, Pageable pageable);
    
    @Query("select t from Payment t where t.id=:id ")
    
    Payment  findById(Long id);
    void deleteById(Long id);
    
    @Query("SELECT t FROM Payment t WHERE t.receipt.id = :receipt OR :receipt = 0")
    List<Payment> findById2(Long receipt);
    
}
