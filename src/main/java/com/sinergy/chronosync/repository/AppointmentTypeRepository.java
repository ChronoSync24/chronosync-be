package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  AppointmentType repository class for managing appointment types.
 */

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {
}
