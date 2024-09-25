package com.document.repository;
import com.document.entity.PaymentType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer> {

	@Query("select t from PaymentType t where t.name LIKE %:name% and t.status LIKE %:status% ")
	Page<PaymentType> findAllByCriteria(String name, String status, Pageable pageable);

    
    @Query("select t from PaymentType t where t.id=:id ")
    
    PaymentType  findById(Long id);
    void deleteById(Long id);

    @Query("SELECT t FROM PaymentType t WHERE t.id = :payment_type OR :payment_type = 0")
    List<PaymentType> findById2(Long payment_type);
}
