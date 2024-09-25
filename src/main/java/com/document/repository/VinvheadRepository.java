package com.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.document.entity.views.vinvhead;

@Repository
public interface VinvheadRepository extends JpaRepository<vinvhead, Long> {

	@Query("select t from vinvhead t "
			+ "where t.invHead = :invHead")
	List<vinvhead> findDataReport(Long invHead);
}
