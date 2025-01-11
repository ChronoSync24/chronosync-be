package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.AppointmentTypeFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.firm.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing appointment types.
 * <p>This service handles all business logic related to appointment types, including
 * retrieving, creating, updating, and deleting appointment type entities.</p>
 */
@Service
@AllArgsConstructor
public class AppointmentTypeServiceImpl implements AppointmentTypeService {

	private final AppointmentTypeRepository appointmentTypeRepository;
	private final UserRepository userRepository;

	/**
	 * Retrieves all appointment types associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link AppointmentType} objects linked to that firm's ID.
	 *
	 * @return {@link Page} of {@link AppointmentType} objects associated with the current user's firm.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws InvalidStateException if the user is not assigned to a firm.
	 */
	public Page<AppointmentType> getAppointmentTypes(PageRequest pageRequest) {
		AppointmentTypeFilterBuilder filterBuilder = AppointmentTypeFilterBuilder.builder()
			.firmId(getAuthUserFirm().getId())
			.build();

		filterBuilder.setPageable(pageRequest);

		return appointmentTypeRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());
	}

	/**
	 * Creates a new appointment type and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details.
	 * @return {@link AppointmentType} representing the saved appointment type.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws InvalidStateException if the user is not associated with a firm.
	 */
	@Override
	public AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDto) {
		AppointmentType appointmentType = requestDto.toModel();
		appointmentType.setFirm(getAuthUserFirm());

		return appointmentTypeRepository.save(appointmentType);
	}

	/**
	 * Updates an existing appointment type or creates a new one if no ID is provided.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} representing the updated or newly created appointment type
	 * @throws UserNotFoundException if the user cannot be found.
	 * @throws InvalidStateException if the appointment type cannot be found for update.
	 */
	@Override
	public AppointmentType updateAppointmentType(AppointmentTypeRequestDTO requestDto) {
		AppointmentType existingAppointmentType = appointmentTypeRepository.findById(requestDto.getId())
			.orElseThrow(
				() -> new InvalidStateException("Appointment type with ID " + requestDto.getId() + " does not exist.")
			);

		if (!existingAppointmentType.getFirm().getId().equals(getAuthUserFirm().getId())) {
			throw new InvalidStateException("Appointment type does not belong to the current user's firm.");
		}

		return appointmentTypeRepository.save(requestDto.toModel());
	}

	/**
	 * Deletes an appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws InvalidStateException if deletion fails or the appointment type does not exist
	 */
	@Override
	public void deleteAppointmentType(Long id) {
		if (!appointmentTypeRepository.existsById(id)) {
			throw new InvalidStateException("Appointment type does not exist.");
		}
		appointmentTypeRepository.deleteById(id);
	}

	private Firm getAuthUserFirm() {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(SecurityContextHolder.getContext().getAuthentication().getName())
			.build();

		User user = userRepository.findOne(filterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		return firm;
	}
}
