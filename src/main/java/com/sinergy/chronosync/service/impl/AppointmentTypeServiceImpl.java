package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing appointment types.
 *
 * <p>This service handles all business logic related to appointment types, including
 * retrieving, creating, updating, and deleting appointment type entities.</p>
 */
@Service
public class AppointmentTypeServiceImpl {

	private final AppointmentTypeRepository appointmentTypeRepository;

	/**
	 * Constructs the AppointmentTypeServiceImpl with the required repository.
	 *
	 * @param appointmentTypeRepository {@link AppointmentTypeRepository} for accessing the database
	 */
	@Autowired
	public AppointmentTypeServiceImpl(AppointmentTypeRepository appointmentTypeRepository) {
		this.appointmentTypeRepository = appointmentTypeRepository;
	}

	/**
	 * Retrieves all appointment types stored in the database.
	 *
	 * @return {@link List} of {@link AppointmentType} containing all appointment types
	 */
	public List<AppointmentType> getAppointmentTypes() {
		return appointmentTypeRepository.findAll();
	}

	/**
	 * Creates a new appointment type and stores it in the database.
	 *
	 * @param requestDTO {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} representing the saved appointment type
	 */
	public AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDTO) {
		AppointmentType appointmentType = mapRequestToModel(requestDTO);
		return appointmentTypeRepository.save(appointmentType);
	}

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * @param id         {@link Long} ID of the appointment type to update
	 * @param requestDTO {@link AppointmentTypeRequestDTO} containing updated details
	 * @return {@link AppointmentType} representing the updated appointment type
	 * @throws IllegalArgumentException if no appointment type with the given ID exists
	 */
	public AppointmentType updateAppointmentType(Long id, AppointmentTypeRequestDTO requestDTO) {
		Optional<AppointmentType> optionalAppointmentType = appointmentTypeRepository.findById(id);

		if (optionalAppointmentType.isEmpty()) {
			throw new IllegalArgumentException("Appointment type with ID " + id + " does not exist.");
		}

		AppointmentType appointmentType = optionalAppointmentType.get();
		appointmentType.setName(requestDTO.getName());
		appointmentType.setDurationMinutes(requestDTO.getDurationMinutes());
		appointmentType.setPrice(requestDTO.getPrice());
		appointmentType.setCurrency(requestDTO.getCurrency());

		return appointmentTypeRepository.save(appointmentType);
	}

	/**
	 * Deletes an appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws IllegalArgumentException if no appointment type with the given ID exists
	 */
	public void deleteAppointmentType(Long id) {
		if (!appointmentTypeRepository.existsById(id)) {
			throw new IllegalArgumentException("Appointment type with ID " + id + " does not exist.");
		}
		appointmentTypeRepository.deleteById(id);
	}

	/**
	 * Maps an {@link AppointmentTypeRequestDTO} to an {@link AppointmentType} entity.
	 *
	 * @param requestDTO {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} entity
	 */
	private AppointmentType mapRequestToModel(AppointmentTypeRequestDTO requestDTO) {
		AppointmentType appointmentType = new AppointmentType();
		appointmentType.setName(requestDTO.getName());
		appointmentType.setDurationMinutes(requestDTO.getDurationMinutes());
		appointmentType.setPrice(requestDTO.getPrice());
		appointmentType.setCurrency(requestDTO.getCurrency());
		return appointmentType;
	}
}
