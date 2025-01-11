package com.sinergy.chronosync.controller;

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
 *
 * <p>This test class validates the functionality of the {@link UserController},
 * specifically testing the `enable` endpoint.</p>
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
	 * Tests the {@link UserController#enable(Long)} method.
	 * Verifies that the user service is called with the correct user ID
	 * and that the response is properly constructed.
	 */
	@Test
	void enableUserTest() {
		Long userId = 1L;

		doNothing().when(userService).enable(userId);

		ResponseEntity<String> result = userController.enable(userId);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody()).isEqualTo("User successfully enabled.");

		verify(userService, times(1)).enable(userId);
	}
}
