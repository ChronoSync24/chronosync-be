package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing appointment types.
 *
 * This controller provides endpoints for retrieving, creating, updating, and deleting appointment types.
 * It delegates the business logic to the {@link AppointmentTypeServiceImpl}.
 */
@RestController
@RequestMapping(path = "api/v1/appointmenttype")
public class AppointmentTypeController {

	private final AppointmentTypeServiceImpl appointmentTypeService;

	/**
	 * Constructs the AppointmentTypeController with the required service.
	 *
	 * @param appointmentTypeService {@link AppointmentTypeServiceImpl} handling business logic
	 */
	@Autowired
	public AppointmentTypeController(AppointmentTypeServiceImpl appointmentTypeService) {
		this.appointmentTypeService = appointmentTypeService;
	}

	/**
	 * Retrieves all appointment types.
	 *
	 * @return {@link List} of {@link AppointmentType} containing all appointment types
	 */
	@GetMapping
	public List<AppointmentType> getAppointmentTypes() {
		return appointmentTypeService.getAppointmentTypes();
	}

	/**
	 * Creates a new appointment type.
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link ResponseEntity} containing the created {@link AppointmentType} and HTTP status
	 */
	@PostMapping
	public ResponseEntity<AppointmentType> createAppointmentType(@RequestBody AppointmentTypeRequestDTO request) {
		AppointmentType createdAppointmentType = appointmentTypeService.createAppointmentType(request);
		return new ResponseEntity<>(createdAppointmentType, HttpStatus.CREATED);
	}

	/**
	 * Updates an existing appointment type by its ID.
	 *
	 * @param id      {@link Long} ID of the appointment type to update
	 * @param request {@link AppointmentTypeRequestDTO} containing updated appointment type details
	 * @return {@link ResponseEntity} containing the updated {@link AppointmentType} and HTTP status
	 */
	@PutMapping
	public ResponseEntity<AppointmentType> updateAppointmentType(
		@RequestParam Long id,
		@RequestBody AppointmentTypeRequestDTO request) {
		AppointmentType updatedAppointmentType = appointmentTypeService.updateAppointmentType(id, request);
		return ResponseEntity.ok(updatedAppointmentType);
	}

	/**
	 * Deletes an appointment type by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @return {@link ResponseEntity} with HTTP status indicating the result
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteAppointmentType(@RequestParam Long id) {
		appointmentTypeService.deleteAppointmentType(id);
		return ResponseEntity.ok().build();
	}
}
