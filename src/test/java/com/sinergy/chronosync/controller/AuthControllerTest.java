package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AuthenticationRequestDTO;
import com.sinergy.chronosync.dto.request.UserRegisterRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.dto.response.UserRegisterResponseDTO;
import com.sinergy.chronosync.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthController}.
 *
 * <p>This test class validates the functionality of the {@link AuthController},
 * specifically testing the `register` and `authenticate` endpoints.</p>
 */
class AuthControllerTest {

	@Mock
	private AuthenticationService authenticationService;

	@InjectMocks
	private AuthController authController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link AuthController#register(UserRegisterRequestDTO)} method.
	 * Verifies that the registration service is called with the correct request
	 * and that the response is properly constructed.
	 */
	@Test
	void registerUserTest() {
		UserRegisterRequestDTO request = new UserRegisterRequestDTO();
		request.setPassword("password123");
		request.setFirstName("John");
		request.setLastName("Doe");

		UserRegisterResponseDTO response = new UserRegisterResponseDTO(1L, "testUser");

		when(authenticationService.register(any(UserRegisterRequestDTO.class))).thenReturn(response);

		ResponseEntity<UserRegisterResponseDTO> result = authController.register(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getUsername()).isEqualTo("testUser");

		verify(authenticationService, times(1)).register(any(UserRegisterRequestDTO.class));
	}

	/**
	 * Tests the {@link AuthController#authenticate(AuthenticationRequestDTO)} method.
	 * Verifies that the authentication service is called with the correct request
	 * and that the response contains a valid JWT.
	 */
	@Test
	void authenticateUserTest() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO("testUser", "password123");
		AuthenticationResponse response = new AuthenticationResponse("jwtToken123");

		when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(response);

		ResponseEntity<AuthenticationResponse> result = authController.authenticate(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getJwtString()).isEqualTo("jwtToken123");

		verify(authenticationService, times(1)).authenticate(any(AuthenticationRequestDTO.class));
	}
}
