package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.service.impl.LogoutServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LogoutServiceImpl}.
 */
class LogoutServiceTest {

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private LogoutServiceImpl logoutService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the logout functionality with a valid JWT token.
	 * Verifies that the token is deleted and the security context is cleared.
	 */
	@Test
	void successfulLogoutTest() {
		String validJwt = "Bearer jwt123";
		Token token = new Token();

		when(request.getHeader("Authorization")).thenReturn(validJwt);
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any()))
			.thenReturn(Optional.of(token));

		logoutService.logout(request, response, authentication);

		verify(tokenRepository, times(1)).delete(token);
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	/**
	 * Tests the logout functionality when the Authorization header is missing.
	 * Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void missingAuthorizationHeaderTest() {
		when(request.getHeader("Authorization")).thenReturn(null);

		assertThrows(ServiceException.class, () -> logoutService.logout(request, response, authentication));

		verify(tokenRepository, never()).delete(any(Token.class));
	}

	/**
	 * Tests the logout functionality when the JWT token does not start with "Bearer ".
	 * Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void invalidAuthorizationHeaderFormatTest() {
		when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

		assertThrows(ServiceException.class, () -> logoutService.logout(request, response, authentication));

		verify(tokenRepository, never()).delete(any(Token.class));
	}

	/**
	 * Tests the logout functionality when the JWT token in the Authorization header is not found in the repository.
	 * Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void invalidJwtTest() {
		String nonExistentJwt = "Bearer jwt123";

		when(request.getHeader("Authorization")).thenReturn(nonExistentJwt);
		when(tokenRepository.findOne(TokenFilterBuilder.builder().jwtString("jwt123").build().toSpecification()))
			.thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> logoutService.logout(request, response, authentication));

		verify(tokenRepository, never()).delete(any(Token.class));
	}
}

