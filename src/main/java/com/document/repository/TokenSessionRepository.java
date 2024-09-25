package com.document.repository;

import org.springframework.data.repository.Repository;

import com.document.entity.TokenSession;


public interface TokenSessionRepository extends Repository<TokenSession, String> {

    void save(TokenSession tokenSession);

}
