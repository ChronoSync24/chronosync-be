package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.firm.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.AppointmentTypeService;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

	/**
	 * Retrieves all appointment types associated with the current manager's firm.
	 * <p>
	 * This method checks the current logged-in user's (manager's) firm and returns
	 * a list of {@link AppointmentType} objects linked to that firm's ID.
	 *
	 * @return {@link List} of {@link AppointmentType} objects associated with the current manager's firm.
	 * @throws ResponseStatusException if the manager is not found or is not assigned to a firm.
	 */
	public List<AppointmentType> getAppointmentTypesForCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User manager = userRepository.findByUsername(username)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));

		if (manager.getFirm() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager is not assigned to a firm");
		}
		return appointmentTypeRepository.findByFirm_Id(manager.getFirm().getId());
	}

	/**
	 * Creates a new appointment type and stores it in the database.
	 *
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing appointment type details
	 * @return {@link AppointmentType} representing the saved appointment type
	 * @throws ServiceException if the user is not found or not associated with any firm
	 */
	@Override
	public AppointmentType createAppointmentType(AppointmentTypeRequestDTO requestDto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new ServiceException("User not found with username: " + username));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new ServiceException("User is not associated with any firm.");
		}
		AppointmentType appointmentType = requestDto.toModel();
		appointmentType.setFirm(firm);
		return appointmentTypeRepository.save(appointmentType);
	}

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * @param id         {@link Long} ID of the appointment type to update
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing updated details
	 * @return {@link AppointmentType} representing the updated appointment type
	 * @throws ServiceException if the appointment type or firm cannot be found
	 */
	@Override
	public AppointmentType updateAppointmentType(Long id, AppointmentTypeRequestDTO requestDto) {

		AppointmentType existingAppointmentType = appointmentTypeRepository.findById(id)
			.orElseThrow(() -> new ServiceException("Appointment type with ID " + id + " does not exist."));
		AppointmentType updatedAppointmentType = requestDto.toModel();
		updatedAppointmentType.setFirm(existingAppointmentType.getFirm());
		updatedAppointmentType.setId(existingAppointmentType.getId());
		return appointmentTypeRepository.save(updatedAppointmentType);
	}

	/**
	 * Deletes an appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws ServiceException if deletion fails or the appointment type does not exist
	 */
	@Override
	public void deleteAppointmentType(Long id) {
		if (!appointmentTypeRepository.existsById(id)) {
			throw new ServiceException("Appointment type with ID " + id + " does not exist.");
		}
		appointmentTypeRepository.deleteById(id);
	}
}
