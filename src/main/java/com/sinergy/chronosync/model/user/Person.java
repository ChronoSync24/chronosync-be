package com.sinergy.chronosync.model.user;

import com.sinergy.chronosync.model.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract Person class.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Person extends BaseEntity {

	private String firstName;
	private String lastName;
	private String identificationNumber;
	private String address;
	private String phone;
	private String email;
}
