package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing appointment types.
 * <p>
 * This controller provides endpoints for retrieving, creating, updating, and deleting appointment types.
 * It integrates with the {@link AppointmentTypeServiceImpl} and {@link UserRepository}.
 * </p>
 */
@RestController
@RequestMapping(path = "api/v1/appointmenttype")
@RequiredArgsConstructor
public class AppointmentTypeController {

	private final AppointmentTypeServiceImpl appointmentTypeServiceImpl;

	/**
	 * Retrieves all appointment types associated with the current user's firm.
	 *
	 * @return {@link List} of {@link AppointmentType} objects associated with the current manager's firm.
	 */
	@GetMapping
	public ResponseEntity<List<AppointmentType>> getAppointmentTypes(

	) {
		List<AppointmentType> appointmentTypes = appointmentTypeServiceImpl.getAppointmentTypesForCurrentUser();
		return ResponseEntity.ok(appointmentTypes);
	}

	/**
	 * Creates new appointment type for the current user's firm.
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing the details of the new appointment type
	 * @return created {@link AppointmentType} along with an HTTP status of 201 (Created)
	 */
	@PostMapping
	public ResponseEntity<AppointmentType> createAppointmentType(
		@RequestBody AppointmentTypeRequestDTO request
	) {
		AppointmentType appointmentType = appointmentTypeServiceImpl.createAppointmentType(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentType);
	}

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * @param id      {@link Long} ID of the appointment type to update
	 * @param request {@link AppointmentTypeRequestDTO} containing updated details of the appointment type
	 * @return {@link ResponseEntity} containing the updated {@link AppointmentType} and HTTP status
	 */
	@PutMapping
	public ResponseEntity<AppointmentType> updateAppointmentType(
		@RequestParam Long id,
		@RequestBody AppointmentTypeRequestDTO request
	) {
		AppointmentType updatedAppointmentType = appointmentTypeServiceImpl.updateAppointmentType(id, request);
		return ResponseEntity.ok(updatedAppointmentType);
	}

	/**
	 * Deletes an appointment type by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteAppointmentType(
		@RequestParam Long id
	) {
		appointmentTypeServiceImpl.deleteAppointmentType(id);
		return ResponseEntity.noContent().build();
	}
}
