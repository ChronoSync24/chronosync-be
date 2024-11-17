package com.sinergy.chronosync.model.appointmentType;

import com.sinergy.chronosync.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Entity;

import java.util.Collection;
import java.util.Currency;
import java.util.List;

/**
 * Appointment type model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "appointmentType")
public class AppointmentType extends BaseEntity {

	//TODO: Replace attribute types of appointmentPrice and currency

	private String name;
	private Integer durationMinutes;
	private Double price;
	private String currency; // Store currency code (e.g., "EUR", "BAM", "USD"...)

}
