package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.AuthenticationRequestDTO;
import com.sinergy.chronosync.dto.request.UserRegisterRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.dto.response.UserRegisterResponseDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthenticationService}.
 *
 * <p>Tests various methods of the {@link AuthenticationService} including user registration,
 * authentication, and error handling. The tests verify the service's behavior under valid and invalid
 * conditions, ensuring the correct interactions with repositories, authentication managers, and utility classes.</p>
 */
class AuthenticationServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

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
	 * Tests the {@link AuthenticationServiceImpl#register(UserRegisterRequestDTO)} method.
	 * Verifies that a user is registered successfully and that the correct methods are called
	 * for encoding the password and saving the user to the repository.
	 */
	@Test
	void registerUserTest() {
		UserRegisterRequestDTO request = new UserRegisterRequestDTO();
		request.setFirstName("Test");
		request.setLastName("Test");

		User user = request.toModel();
		user.setPassword("encodedPassword");

		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);

		UserRegisterResponseDTO response = authenticationService.register(request);

		assertThat(response.getUsername()).isEqualTo(request.getUsername());

		verify(userRepository, times(1)).save(any(User.class));
		verify(passwordEncoder, times(1)).encode(request.getPassword());
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(AuthenticationRequestDTO)} method
	 * with valid user credentials. Verifies that a JWT token is generated and returned in the response.
	 */
	@Test
	void authenticateValidUserTest() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO("test", "password123");
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
	 * Tests the {@link AuthenticationServiceImpl#authenticate(AuthenticationRequestDTO)} method
	 * when a valid user is authenticated but lacks an existing token. Verifies that a new token is created
	 * and saved in the repository.
	 */
	@Test
	void authenticateValidUserWithNewTokenTest() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO("test", "password123");
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
	 * Tests the {@link AuthenticationServiceImpl#authenticate(AuthenticationRequestDTO)} method
	 * with invalid user credentials. Verifies that a {@link ServiceException} is thrown for invalid credentials.
	 */
	@Test
	void invalidCredentialsTest() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO("invalidUser", "password123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenThrow(new BadCredentialsException("Invalid credentials."));

		assertThrows(ServiceException.class, () -> authenticationService.authenticate(request));

		verify(authenticationManager, times(1))
			.authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	/**
	 * Tests the {@link AuthenticationServiceImpl#authenticate(AuthenticationRequestDTO)} method
	 * when authentication fails due to a missing user. Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void authenticationFailedTest() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO("invalidUser", "password123");

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