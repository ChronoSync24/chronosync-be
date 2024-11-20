package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import java.util.List;

public interface AppointmentTypeService {

	List<AppointmentType> getAppointmentTypesByFirmId(Long id);
	AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDto);
	AppointmentType updateAppointmentType(Long id, AppointmentTypeRequestDTO requestDto);
	void deleteAppointmentType(Long id);

}