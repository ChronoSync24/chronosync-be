package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.firm.Firm;
import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter builder for creating specifications to query {@link Firm} entities.
 *
 * <p>This class dynamically constructs predicates based on the provided
 * filter values. If a filter value is null or empty, it is ignored.</p>
 */
@Builder
public class FirmFilterBuilder {

	private static final String ID = "id";
	private static final String NAME = "name";

	private Long id;
	private String name;

	/**
	 * Converts the filter criteria into a {@link Specification} for querying {@link Firm} entities.
	 *
	 * @return a {@link Specification} that can be used to filter {@link Firm} entities
	 */
	public Specification<Firm> toSpecification() {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (id != null) {
				predicates.add(criteriaBuilder.equal(root.get(ID), id));
			}
			if (name != null && !name.isBlank()) {
				predicates.add(criteriaBuilder.like(root.get(NAME), "%" + name + "%"));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}