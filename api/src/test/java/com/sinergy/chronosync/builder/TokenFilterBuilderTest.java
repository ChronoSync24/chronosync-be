package com.sinergy.chronosync.builder;

import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TokenFilterBuilder}.
 *
 * <p>Verifies the behavior of the {@link TokenFilterBuilder} class,
 * specifically its ability to convert filter criteria into a {@link Specification}
 * for querying {@link Token} entities based on JWT string and associated user.</p>
 *
 * <p>Mock objects are used to simulate the database environment, including
 * {@link Root}, {@link CriteriaBuilder}, and {@link Predicate} instances.</p>
 */
public class TokenFilterBuilderTest {

	@Mock
	private Root<Token> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private Predicate predicate;

	@Mock
	private Path<String> jwtStringPath;

	@Mock
	private Path<User> userPath;

	@InjectMocks
	private TokenFilterBuilder.TokenFilterBuilderBuilder tokenFilterBuilderBuilder;

	private User user;
	private String jwtString;

	private AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		user = new User();
		jwtString = "sample_jwt";
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	/**
	 * Verifies that the {@link TokenFilterBuilder#toSpecification()} method correctly builds
	 * a {@link Specification} based on provided JWT string and user values.
	 *
	 * <p>Checks if the appropriate predicates are added to the
	 * {@link CriteriaBuilder} based on the filter criteria in {@link TokenFilterBuilder}.
	 * Mocks are used to simulate the behavior of each method, ensuring that the correct
	 * criteria methods are called.</p>
	 */
	@Test
	void toSpecificationTest() {
		TokenFilterBuilder tokenFilterBuilder = tokenFilterBuilderBuilder
			.jwtString(jwtString)
			.user(user)
			.build();

		when(root.<String>get("jwtString")).thenReturn(jwtStringPath);
		when(root.<User>get("user")).thenReturn(userPath);

		when(criteriaBuilder.equal(jwtStringPath, jwtString)).thenReturn(predicate);
		when(criteriaBuilder.equal(userPath, user)).thenReturn(predicate);
		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

		Specification<Token> specification = tokenFilterBuilder.toSpecification();
		assertNotNull(specification);

		specification.toPredicate(root, query, criteriaBuilder);

		verify(criteriaBuilder).equal(jwtStringPath, jwtString);
		verify(criteriaBuilder).equal(userPath, user);
		verify(criteriaBuilder).and(any(Predicate[].class));
	}
}
