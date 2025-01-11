package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.UserCreateRequestDTO;
import com.sinergy.chronosync.dto.response.UserCreateResponseDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.UserServiceImpl;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link UserServiceImpl#create(UserCreateRequestDTO)} method.
	 * Verifies that a user is created successfully and that the correct methods are called
	 * for encoding the password and saving the user to the repository.
	 */
	@Test
	void createUserTest() {
		UserCreateRequestDTO request = new UserCreateRequestDTO();
		request.setFirstName("Test");
		request.setLastName("Test");

		User user = request.toModel();
		user.setPassword("encodedPassword");

		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);

		UserCreateResponseDTO response = userService.create(request);

		assertThat(response.getUsername()).isEqualTo(request.getUsername());

		verify(userRepository, times(1)).save(any(User.class));
		verify(passwordEncoder, times(1)).encode(request.getPassword());
	}

	/**
	 * Tests the {@link UserServiceImpl#enable(Long)} method when a user with the specified ID exists.
	 * Verifies that the user is enabled and saved in the repository.
	 */
	@Test
	void enableUserTest() {
		Long validUserId = 1L;
		User user = new User();
		user.setId(validUserId);
		user.setIsEnabled(false);

		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));

		userService.enable(validUserId);

		assertTrue(user.getIsEnabled());

		verify(userRepository, times(1)).save(user);
	}

	/**
	 * Tests the {@link UserServiceImpl#enable(Long)} method when no user is found for the given ID.
	 * Verifies that a {@link ServiceException} is thrown.
	 */
	@Test
	void enableInvalidUserTest() {
		Long invalidUserId = 99L;

		when(userRepository.findOne(UserFilterBuilder.builder().id(invalidUserId).build().toSpecification()))
			.thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> userService.enable(invalidUserId));

		verify(userRepository, never()).save(any(User.class));
	}
}
