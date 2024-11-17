package com.sinergy.chronosync.dto.request;

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
	private String currency; // Currency code, e.g., "EUR", "USD"
}