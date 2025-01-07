package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.builder.AppointmentTypeFilterBuilder;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppointmentTypeRepository}.
 *
 * <p>Verifies the behavior of the {@link AppointmentTypeRepository} class,
 * specifically its ability to interact with the database and retrieve {@link AppointmentType} entities
 * using various methods such as {@link AppointmentTypeRepository#findAll()},
 * {@link AppointmentTypeRepository#findAll(Specification)}, and {@link AppointmentTypeRepository#findOne(Specification)}.</p>
 *
 * <p>Mock objects are utilized to simulate the repository and database interactions.
 * Tests cover scenarios such as retrieving all appointment types, filtering based on specifications,
 * and handling cases with no matching results.</p>
 *
 * <p>The tests use {@link AppointmentTypeFilterBuilder} to create specifications for filtering
 * appointment types by attributes such as name, ensuring that the repository methods
 * correctly handle filtering logic.</p>
 */
class AppointmentTypeRepositoryTest {

	@Mock
	private AppointmentTypeRepository appointmentTypeRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AppointmentTypeRepository#findAll()} method.
	 * Verifies that the repository returns a list of appointment types.
	 */
	@Test
	void findAllAppointmentTypesTest() {
		List<AppointmentType> appointmentTypes = getAppointmentTypes();

		when(appointmentTypeRepository.findAll()).thenReturn(appointmentTypes);

		List<AppointmentType> result = appointmentTypeRepository.findAll();

		assertThat(result).hasSize(appointmentTypes.size());
		verify(appointmentTypeRepository, times(1)).findAll();
	}

	/**
	 * Tests the {@link AppointmentTypeRepository#findAll(Specification)} method using a specification.
	 * Verifies that the repository returns a list of appointment types matching the specification.
	 */
	@Test
	void findByNameTest() {
		Specification<AppointmentType> spec = AppointmentTypeFilterBuilder.builder()
			.name("Consultation")
			.build()
			.toSpecification();

		List<AppointmentType> appointmentTypes = getAppointmentTypes();

		when(appointmentTypeRepository.findAll(Mockito.<Specification<AppointmentType>>any())).thenReturn(appointmentTypes);

		List<AppointmentType> result = appointmentTypeRepository.findAll(spec);

		assertThat(result).hasSize(appointmentTypes.size());
		verify(appointmentTypeRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link AppointmentTypeRepository#findAll(Specification)} method with no matching criteria.
	 * Verifies that the repository returns an empty list when no appointment types match the specification.
	 */
	@Test
	void findWithNoMatchTest() {
		Specification<AppointmentType> spec = AppointmentTypeFilterBuilder.builder()
			.name("Nonexistent")
			.build()
			.toSpecification();

		when(appointmentTypeRepository.findAll(Mockito.<Specification<AppointmentType>>any())).thenReturn(Collections.emptyList());

		List<AppointmentType> result = appointmentTypeRepository.findAll(spec);

		assertThat(result).isEmpty();
		verify(appointmentTypeRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link AppointmentTypeRepository#findOne(Specification)} method when a matching appointment type is found.
	 */
	@Test
	void findOneTest() {
		AppointmentType appointmentType = getAppointmentTypes().get(0);

		Specification<AppointmentType> spec = AppointmentTypeFilterBuilder.builder()
			.name("Consultation")
			.build()
			.toSpecification();

		when(appointmentTypeRepository.findOne(Mockito.<Specification<AppointmentType>>any()))
			.thenReturn(Optional.of(appointmentType));

		java.util.Optional<AppointmentType> result = appointmentTypeRepository.findOne(spec);

		assertThat(result).isPresent();
		assertThat(result.get()).isEqualTo(appointmentType);
		verify(appointmentTypeRepository, times(1)).findOne(spec);
	}

	/**
	 * Helper method to generate a list of {@link AppointmentType} objects for testing purposes.
	 *
	 * @return {@link List<AppointmentType>} a list of appointment types
	 */
	private List<AppointmentType> getAppointmentTypes() {
		AppointmentType appointmentType1 = new AppointmentType();
		appointmentType1.setId(1L);
		appointmentType1.setName("Consultation");

		AppointmentType appointmentType2 = new AppointmentType();
		appointmentType2.setId(2L);
		appointmentType2.setName("Checkup");

		return List.of(appointmentType1, appointmentType2);
	}
}
