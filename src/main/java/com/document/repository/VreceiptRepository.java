package com.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.document.entity.views.vreceipt;

@Repository
public interface VreceiptRepository extends JpaRepository<vreceipt, Long> {

	@Query("select t from vreceipt t "
			+ "where t.receipt = :receipt")
	List<vreceipt> findDataReport(Long receipt);
}
