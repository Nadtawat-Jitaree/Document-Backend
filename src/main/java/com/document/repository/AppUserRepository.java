package com.document.repository;

import com.document.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    @Query("select t from AppUser t where t.userName LIKE %:userName%")
    Page<AppUser> findAllByCriteria(String userName, Pageable pageable);

    AppUser findByUserNameAndPassword(String userName, String password);
    AppUser findByUserName(String userName);
    AppUser findById(Long id);
    void deleteByUserName(String userName);

}
