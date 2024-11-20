package com.sinergy.chronosync.model.appointmentType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sinergy.chronosync.model.BaseEntity;
import com.sinergy.chronosync.model.firm.Firm;
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
import java.util.List;

/**
 * Appointment type model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "appointmentTypes")
public class AppointmentType extends BaseEntity {

	private String name;
	private Integer durationMinutes;
	private Double price;
	private String colourCode;
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "firm_id")
	@JsonBackReference
	private Firm firm;
}
