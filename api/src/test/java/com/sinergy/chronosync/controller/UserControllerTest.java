package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.UserCreateRequestDTO;
import com.sinergy.chronosync.dto.response.UserCreateResponseDTO;
import com.sinergy.chronosync.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserController}.
 */
class UserControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link UserController#create(UserCreateRequestDTO)} method.
	 * Verifies that the creation service is called with the correct request
	 * and that the response is properly constructed.
	 */
	@Test
	void createUserTest() {
		UserCreateRequestDTO request = new UserCreateRequestDTO();
		request.setPassword("password123");
		request.setFirstName("John");
		request.setLastName("Doe");

		UserCreateResponseDTO response = new UserCreateResponseDTO(1L, "testUser");

		when(userService.create(any(UserCreateRequestDTO.class))).thenReturn(response);

		ResponseEntity<UserCreateResponseDTO> result = userController.create(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getUsername()).isEqualTo("testUser");

		verify(userService, times(1)).create(any(UserCreateRequestDTO.class));
	}
}
