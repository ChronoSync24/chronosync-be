package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository class for tokens management.
 */
public interface TokenRepository extends JpaRepository<Token, Integer>, JpaSpecificationExecutor<Token> {

}
