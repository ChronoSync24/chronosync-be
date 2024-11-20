package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.appointmentType.Currency;
import com.sinergy.chronosync.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating an appointment type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTypeRequestDTO {

	private String name;
	private Integer durationMinutes;
	private Double price;
	private Currency currency;
	private String colourCode;

	/**
	 * Creates and returns AppointmentType model from Data Transfer Object.
	 *
	 * @return {@link AppointmentType} user model
	 */
	public AppointmentType toModel(){
		AppointmentType appointmentType = new AppointmentType();
		appointmentType.setName(name);
		appointmentType.setDurationMinutes(durationMinutes);
		appointmentType.setPrice(price);
		appointmentType.setCurrency(currency);
		appointmentType.setColourCode(colourCode);

		return appointmentType;
	}
}