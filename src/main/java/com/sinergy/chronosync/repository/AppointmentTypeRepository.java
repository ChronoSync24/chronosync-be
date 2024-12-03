package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link AppointmentType} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations for {@link AppointmentType} entities.
 */
@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long>, JpaSpecificationExecutor<AppointmentType> {
}

