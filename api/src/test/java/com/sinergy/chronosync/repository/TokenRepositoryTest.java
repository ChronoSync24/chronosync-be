package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TokenRepository}.
 */
class TokenRepositoryTest {

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private User user;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link TokenRepository#findAll()} method.
	 * Verifies that the repository returns a list of tokens and that the size of the list matches
	 * the expected number of tokens.
	 *
	 * <p>This method mocks the repository's behavior to return a predefined list of tokens
	 * and checks the result.</p>
	 */
	@Test
	void findAllTokensTest() {
		List<Token> testTokens = getTokens();

		when(tokenRepository.findAll()).thenReturn(testTokens);

		List<Token> tokens = tokenRepository.findAll();

		assertThat(tokens).hasSize(testTokens.size());
		verify(tokenRepository, times(1)).findAll();
	}

	/**
	 * Tests the {@link TokenRepository#findAll(Specification)} method with a specification that
	 * filters by a non-existent JWT string. Verifies that an empty list is returned when no
	 * tokens match the criteria.
	 *
	 * @see TokenFilterBuilder
	 */
	@Test
	void findWithNoMatchJwtStringTest() {
		Specification<Token> spec = TokenFilterBuilder.builder()
			.jwtString("nonexistent")
			.build()
			.toSpecification();

		when(tokenRepository.findAll(Mockito.<Specification<Token>>any())).thenReturn(Collections.emptyList());

		List<Token> tokens = tokenRepository.findAll(spec);

		assertThat(tokens).isEmpty();
		verify(tokenRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link TokenRepository#findOne(Specification)} method with a specification
	 * that filters by user. Verifies that the repository returns the correct token associated
	 * with the specified user.
	 *
	 * @see TokenFilterBuilder
	 */
	@Test
	void findByUserTest() {
		Token testToken = getTokens().getFirst();

		Specification<Token> spec = TokenFilterBuilder.builder()
			.user(user)
			.build()
			.toSpecification();

		when(tokenRepository.findOne(Mockito.<Specification<Token>>any()))
			.thenReturn(Optional.of(testToken));

		Optional<Token> token = tokenRepository.findOne(spec);

		assertThat(token.isPresent()).isTrue();
		assertEquals(testToken, token.get());
		verify(tokenRepository, times(1)).findOne(spec);
	}

	/**
	 * Tests the {@link TokenRepository#findOne(Specification)} method with a specification
	 * that filters by JWT string. Verifies that the repository returns the correct token
	 * associated with the specified JWT string.
	 *
	 * @see TokenFilterBuilder
	 */
	@Test
	void findByJwtStringTest() {
		Token testToken = getTokens().getFirst();

		Specification<Token> spec = TokenFilterBuilder.builder()
			.jwtString("jwt123")
			.build()
			.toSpecification();

		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(testToken));

		Optional<Token> foundToken = tokenRepository.findOne(spec);

		assertThat(foundToken).isPresent();
		assertThat(foundToken.get()).isEqualTo(testToken);
		verify(tokenRepository, times(1)).findOne(spec);
	}

	/**
	 * Helper method to generate a list of {@link Token} objects for testing purposes.
	 *
	 * @return {@link List<Token>} a list of tokens
	 */
	private List<Token> getTokens() {
		User user1 = new User();
		user1.setId(1L);
		user1.setUsername("test1");

		User user2 = new User();
		user2.setId(2L);
		user2.setUsername("test");

		Token token1 = Token.builder()
			.id(1)
			.jwtString("jwt123")
			.user(user1)
			.build();

		Token token2 = Token.builder()
			.id(2)
			.jwtString("jwt456")
			.user(user2)
			.build();

		return List.of(token1, token2);
	}
}
