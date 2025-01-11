package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AuthenticationServiceImpl;
import com.sinergy.chronosync.util.JwtUtils;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthenticationServiceImpl}.
 */
class AuthenticationServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * with valid user credentials. Verifies that a JWT token is generated and returned in the response.
	 */
	@Test
	void authenticateValidUserTest() {
		LoginRequestDTO request = new LoginRequestDTO("test", "password123");
		User user = new User();
		user.setUsername("test");
		user.setId(1L);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));
		when(jwtUtils.generateJWTString(user)).thenReturn("jwt123");
		when(tokenRepository.findOne(TokenFilterBuilder.builder().user(user).build().toSpecification()))
			.thenReturn(Optional.of(new Token()));

		AuthenticationResponse response = authenticationService.authenticate(request);

		assertThat(response.getJwtString()).isEqualTo("jwt123");

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findOne(Mockito.<Specification<User>>any());
		verify(jwtUtils, times(1)).generateJWTString(user);
		verify(tokenRepository, times(1)).save(any(Token.class));
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * when a valid user is authenticated but lacks an existing token. Verifies that a new token is created
	 * and saved in the repository.
	 */
	@Test
	void authenticateValidUserWithNewTokenTest() {
		LoginRequestDTO request = new LoginRequestDTO("test", "password123");
		User user = new User();
		user.setUsername("test");
		user.setId(1L);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));
		when(jwtUtils.generateJWTString(user)).thenReturn("jwt123");
		when(tokenRepository.findOne(TokenFilterBuilder.builder().user(user).build().toSpecification()))
			.thenReturn(Optional.empty());

		AuthenticationResponse response = authenticationService.authenticate(request);

		assertThat(response.getJwtString()).isEqualTo("jwt123");

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findOne(Mockito.<Specification<User>>any());
		verify(jwtUtils, times(1)).generateJWTString(user);
		verify(tokenRepository, times(1)).save(any(Token.class));
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * with invalid user credentials. Verifies that a {@link ServiceException} is thrown for invalid credentials.
	 */
	@Test
	void invalidCredentialsTest() {
		LoginRequestDTO request = new LoginRequestDTO("invalidUser", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenThrow(new BadCredentialsException("Invalid credentials."));

		assertThrows(ServiceException.class, () -> authenticationService.authenticate(request));

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(LoginRequestDTO)} method
	 * when authentication fails due to a missing user. Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void authenticationFailedTest() {
		LoginRequestDTO request = new LoginRequestDTO("invalidUser", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userRepository.findOne(
			UserFilterBuilder
				.builder()
				.username(request.getUsername())
				.build()
				.toSpecification())
		).thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> authenticationService.authenticate(request));

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findOne(Mockito.<Specification<User>>any());
		verify(tokenRepository, never()).save(any(Token.class));
	}
}