package com.document.repository;


import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.document.entity.AppRole;

@Transactional
public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {

    @Query("select t from AppRole t where t.roleName LIKE %:roleName% and t.roleDesc LIKE %:roleDesc%")
    Page<AppRole> findAllByCriteria(String roleName, String roleDesc, Pageable pageable);

    AppRole findByRoleName(String roleName);
    AppRole findById(Long id);
    void deleteById(Long id);

}
