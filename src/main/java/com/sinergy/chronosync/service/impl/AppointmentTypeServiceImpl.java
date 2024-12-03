package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.FirmFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.firm.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.FirmRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
	private final FirmRepository firmRepository;

	/**
	 * Retrieves all appointment types associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link AppointmentType} objects linked to that firm's ID.
	 *
	 * @return {@link List} of {@link AppointmentType} objects associated with the current user's firm.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws InvalidStateException if the user is not assigned to a firm.
	 */
	public List<AppointmentType> getAppointmentTypesForCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		UserFilterBuilder userFilterBuilder = UserFilterBuilder.builder()
			.username(username)
			.build();

		User currentUser = userRepository.findOne(userFilterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		if (currentUser.getFirm() == null) {
			throw new InvalidStateException("User is not assigned to a firm");
		}

		FirmFilterBuilder firmFilterBuilder = FirmFilterBuilder.builder()
			.id(currentUser.getFirm().getId())
			.build();

		Firm firm = firmRepository.findOne(firmFilterBuilder.toSpecification())
			.orElseThrow(() -> new InvalidStateException("Firm not found"));

		return appointmentTypeRepository.findAll((root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("firm"), firm));
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
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		UserFilterBuilder userFilterBuilder = UserFilterBuilder.builder()
			.username(username)
			.build();

		User user = userRepository.findOne(userFilterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		AppointmentType appointmentType = requestDto.toModel();
		appointmentType.setFirm(firm);

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

		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(SecurityContextHolder.getContext().getAuthentication().getName())
			.build();

		User manager = userRepository.findOne(filterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Firm firm = manager.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		AppointmentType existingAppointmentType = appointmentTypeRepository.findById(requestDto.getId())
			.orElseThrow(() -> new InvalidStateException("Appointment type with ID " + requestDto.getId() + " does not exist."));

		if (!existingAppointmentType.getFirm().equals(firm)) {
			throw new InvalidStateException("Appointment type does not belong to the current user's firm.");
		}

		existingAppointmentType.setName(requestDto.getName());
		existingAppointmentType.setDurationMinutes(requestDto.getDurationMinutes());
		existingAppointmentType.setPrice(requestDto.getPrice());
		existingAppointmentType.setCurrency(requestDto.getCurrency());
		existingAppointmentType.setColorCode(requestDto.getColorCode());

		return appointmentTypeRepository.save(existingAppointmentType);
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
}
