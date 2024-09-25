package com.document.repository;

import com.document.entity.InvDetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

@Transactional
public interface InvDetailRepository extends JpaRepository<InvDetail, Integer> {

	@Query("select t from InvDetail t where (t.quotation.id = :quotation OR :quotation = 0) " +
		       "and (t.invHead.id = :invHead OR :invHead = 0) " +
		       "and (t.receipt.id = :receipt OR :receipt = 0)")
		Page<InvDetail> findAllByCriteria(Long quotation, Long invHead, Long receipt, Pageable pageable);

    
    @Query("select t from InvDetail t where t.id=:id ")
    
    InvDetail  findById(Long id);
    void deleteById(Long id);

}
