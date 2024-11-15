package com.sinergy.chronosync.builder;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for building filter specifications for entities in a repository.
 *
 * <p>Defines the contract for filter builders that convert filter criteria
 * into a {@link Specification} object, which can be used to query the database. Implementing
 * classes should provide the logic for building specifications based on various filter attributes.</p>
 *
 * @param <T> the type of the entity that this filter builder will handle
 */
public interface FilterBuilder<T> {

	/**
	 * Converts the filter criteria defined in the implementing class to a
	 * {@link Specification} for querying the database.
	 *
	 * @return a {@link Specification} that can be used to filter entities of type {@link T}
	 */
	Specification<T> toSpecification();

	/**
	 * Sets the pagination information for the filter builder.
	 *
	 * <p>This method allows the user to customize the pagination settings by providing
	 * a {@link Pageable} object. Implementations can use this information to limit
	 * the number of results returned by a query.</p>
	 *
	 * @param pageable the {@link Pageable} object containing pagination details
	 * @return the current instance of {@link FilterBuilder} for method chaining
	 */
	FilterBuilder<T> setPageable(Pageable pageable);

	/**
	 * Retrieves the current pagination settings.
	 *
	 * @return the {@link Pageable} object that defines the pagination information
	 */
	Pageable getPageable();
}

