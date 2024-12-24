package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentTypeFilterBuilderTest {

	@Mock
	private Root<AppointmentType> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private Predicate predicate;

	@Mock
	private Path<Long> firmIdPath;

	@Mock
	private Path<String> namePath;

	private AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	/**
	 * Verifies that the {@link AppointmentTypeFilterBuilder#toSpecification()} method correctly builds
	 * a {@link Specification} based on provided filter values such as firmId and name.
	 */
	@Test
	void toSpecificationTest() {
		Long firmId = 1L;
		String name = "Test Appointment";

		AppointmentTypeFilterBuilder filterBuilder = AppointmentTypeFilterBuilder.builder()
			.firmId(firmId)
			.name(name)
			.build();

		when(root.<Long>get("firm")).thenReturn(firmIdPath);
		when(root.<String>get("name")).thenReturn(namePath);

		when(firmIdPath.<Long>get("id")).thenReturn(firmIdPath);
		when(criteriaBuilder.equal(firmIdPath, firmId)).thenReturn(predicate);
		when(criteriaBuilder.like(namePath, "%" + name + "%")).thenReturn(predicate);
		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<AppointmentType> specification = filterBuilder.toSpecification();

		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(firmIdPath, firmId);
		verify(criteriaBuilder).like(namePath, "%" + name + "%");
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}

