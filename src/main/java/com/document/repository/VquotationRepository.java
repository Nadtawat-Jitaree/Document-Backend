package com.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.document.entity.views.vquotation;

@Repository
public interface VquotationRepository extends JpaRepository<vquotation, Long> {

	@Query("select t from vquotation t "
			+ "where t.quotation = :quotation")
	List<vquotation> findDataReport(Long quotation);
}
