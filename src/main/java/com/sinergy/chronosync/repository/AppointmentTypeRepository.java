package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link AppointmentType} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations for {@link AppointmentType} entities.
 * Additional query method {@link #findByFirm_Id(Long)} is provided to retrieve appointment types by firm ID.
 */
@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {
	List<AppointmentType> findByFirm_Id(Long id);

}

