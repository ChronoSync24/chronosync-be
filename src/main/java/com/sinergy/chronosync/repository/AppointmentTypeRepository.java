package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {

	// Custom query to find appointment types by firm ID
	List<AppointmentType> findByFirm_Id(Long id);
}
