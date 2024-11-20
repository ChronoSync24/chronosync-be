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
import org.springframework.dao.EmptyResultDataAccessException;
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

	/**
	 * Retrieves all appointment types associated with the specified firm ID.
	 *
	 * This method queries the database for all appointment types that are linked to the
	 * firm identified by the given ID.
	 *
	 * @param id {@link Long} ID of the firm whose appointment types are to be retrieved
	 * @return {@link List} of {@link AppointmentType} containing all appointment types for the firm
	 */
	@Override
	public List<AppointmentType> getAppointmentTypesByFirmId(Long id) {
		return appointmentTypeRepository.findByFirm_Id(id);
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
		try {
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
		} catch (Exception e) {
			throw new ServiceException("Failed to create appointment type: " + e.getMessage(), e);
		}
	}

	/**
	 * Updates an existing appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to update
	 * @param requestDto {@link AppointmentTypeRequestDTO} containing updated details
	 * @return {@link AppointmentType} representing the updated appointment type
	 * @throws ServiceException if updating the appointment type fails
	 */
	@Override
	public AppointmentType updateAppointmentType(Long id, AppointmentTypeRequestDTO requestDto) {
		try {
			AppointmentType existingAppointmentType = appointmentTypeRepository.findById(id)
				.orElseThrow(() -> new ServiceException("Appointment type with ID " + id + " does not exist."));
			AppointmentType updatedAppointmentType = requestDto.toModel();
			updatedAppointmentType.setFirm(existingAppointmentType.getFirm());
			updatedAppointmentType.setId(existingAppointmentType.getId());
			return appointmentTypeRepository.save(updatedAppointmentType);

		} catch (Exception e) {

			throw new ServiceException("Failed to update appointment type with ID " + id + ": " + e.getMessage(), e);
		}
	}

	/**
	 * Deletes an appointment type identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws ServiceException if deletion fails or the appointment type does not exist
	 */
	@Override
	public void deleteAppointmentType(Long id) {
		try {
			appointmentTypeRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {

			throw new ServiceException("Appointment type with ID " + id + " does not exist.", e);
		} catch (Exception e) {

			throw new ServiceException("Failed to delete appointment type with ID " + id + ": " + e.getMessage(), e);
		}
	}
}
