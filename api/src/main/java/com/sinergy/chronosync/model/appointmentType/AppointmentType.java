package com.sinergy.chronosync.model.appointmentType;

import com.sinergy.chronosync.model.BaseEntity;
import com.sinergy.chronosync.model.firm.Firm;
import jakarta.persistence.*;
import lombok.*;

/**
 * Appointment type model class.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "appointmentTypes")
public class AppointmentType extends BaseEntity {

	private String name;
	private Integer durationMinutes;
	private Double price;
	private String colorCode;
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;

}
