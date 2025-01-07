package com.sinergy.chronosync.model.firm;

import com.sinergy.chronosync.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Firm model class.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "firms")
public class Firm extends BaseEntity {
	private String name;
}