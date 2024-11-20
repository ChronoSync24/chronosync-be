package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
@AllArgsConstructor
public class AppointmentTypeController {

	private final AppointmentTypeService appointmentTypeService;
	private final AppointmentTypeServiceImpl appointmentTypeServiceImpl;
	private final UserRepository userRepository;

	/**
	 * Retrieves all appointment types associated with the current manager's firm.
	 *
	 * This method retrieves the appointment types linked to the manager's firm. It returns a
	 * list of {@link AppointmentType} objects associated with the firm's ID.
	 *
	 * @return a {@link List} of {@link AppointmentType} objects associated with the current manager's firm
	 */
	@GetMapping
	public ResponseEntity<List<AppointmentType>> getAppointmentTypes() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User manager = userRepository.findByUsername(username)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));

		if (manager.getFirm() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager is not assigned to a firm");
		}

		List<AppointmentType> appointmentTypes = appointmentTypeServiceImpl.getAppointmentTypesByFirmId(manager.getFirm().getId());
		return ResponseEntity.ok(appointmentTypes);
	}

	/**
	 * Creates a new appointment type for the current manager's firm.
	 *
	 * This method accepts the details for a new appointment type, which will be associated with
	 * the currently authenticated user's firm. It then returns the created appointment type with
	 * an HTTP status of 201 (Created).
	 *
	 * @param request {@link AppointmentTypeRequestDTO} containing the details of the new appointment type
	 * @return the created {@link AppointmentType} along with an HTTP status of 201 (Created)
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AppointmentType createAppointmentType(
		@RequestBody AppointmentTypeRequestDTO request
	) {
		return appointmentTypeServiceImpl.createAppointmentType(request);
	}

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * This method updates the details of an existing appointment type, such as its name,
	 * duration, price, etc., using the provided {@link AppointmentTypeRequestDTO}.
	 *
	 * @param id {@link Long} ID of the appointment type to update
	 * @param request {@link AppointmentTypeRequestDTO} containing updated details of the appointment type
	 * @return a {@link ResponseEntity} containing the updated {@link AppointmentType} and HTTP status
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
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAppointmentType(
		@RequestParam Long id
	) {
		appointmentTypeService.deleteAppointmentType(id);
	}
}
