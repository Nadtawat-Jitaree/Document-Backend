package com.document.repository;

import com.document.entity.AppRoleUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface AppRoleUserRepository extends JpaRepository<AppRoleUser, Integer> {

    @Query("SELECT t FROM AppRoleUser t WHERE t.appUser.userName = :userName")
    List<AppRoleUser> findByUsername(String userName);
    
    @Query("select t from AppRoleUser t where t.appRole.roleName LIKE %:roleName% and t.appRole.roleDesc LIKE %:roleDesc% and t.appUser.userName LIKE %:userName% ")
    Page<AppRoleUser> findAllByCriteria(String roleName, String roleDesc,String userName, Pageable pageable);

    
    AppRoleUser findById(Long id);
    void deleteById(Long id);


}
