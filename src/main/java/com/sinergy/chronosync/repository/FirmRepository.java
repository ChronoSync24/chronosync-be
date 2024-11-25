package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.firm.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Firm} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations for {@link Firm} entities.
 * Spring Data JPA will automatically implement this interface at runtime.
 */
@Repository
public interface FirmRepository extends JpaRepository<Firm, Long> {
}