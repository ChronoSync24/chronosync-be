package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

/**
 * Controller for managing appointment types.
 * <p>
 * This controller provides endpoints for retrieving, creating, updating, and deleting appointment types.
 * </p>
 */
@RestController
@RequestMapping(path = "api/v1/appointmenttype")
@RequiredArgsConstructor
public class AppointmentTypeController {

	private final AppointmentTypeServiceImpl appointmentTypeService;

	/**
	 * Retrieves all appointment types associated with the current user's firm.
	 *
	 * <p>This endpoint is intended for users associated with a firm to view the
	 * appointment types specific to their firm. It ensures data isolation by returning only
	 * the appointment types linked to the logged-in user's firm.</p>
	 *
	 * @return {@link Page} of {@link AppointmentType} objects associated with the current manager's firm.
	 */
	@GetMapping
	public ResponseEntity<Page<AppointmentType>> getAppointmentTypes(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Page<AppointmentType> appointmentTypes = appointmentTypeService.getAppointmentTypesForUser(page, size);
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
		AppointmentType appointmentType = appointmentTypeService.createAppointmentType(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentType);
	}

	/**
	 * Updates an existing appointment type identified by its ID, or creates a new one if no ID is provided.
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing details of the appointment type to update or create
	 * @return {@link ResponseEntity} containing the updated or created {@link AppointmentType} and HTTP status
	 */
	@PutMapping
	public ResponseEntity<AppointmentType> updateAppointmentType(
		@RequestBody AppointmentTypeRequestDTO request
	) {
		AppointmentType updatedAppointmentType = appointmentTypeService.updateAppointmentType(request);
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
		appointmentTypeService.deleteAppointmentType(id);
		return ResponseEntity.noContent().build();
	}
}
