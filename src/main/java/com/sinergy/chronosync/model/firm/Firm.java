package com.sinergy.chronosync.model.firm;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sinergy.chronosync.model.BaseEntity;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "firms")
public class Firm extends BaseEntity {
	private String name;
}