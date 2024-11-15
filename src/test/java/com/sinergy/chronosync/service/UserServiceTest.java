package com.sinergy.chronosync.service;

import com.sinergy.chronosync.builder.UserFilterBuilder;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService}.
 *
 * <p>This test class validates the functionality of the {@link UserService},
 * specifically focusing on the `enable` method. It ensures proper handling of
 * scenarios like enabling a valid user and throwing exceptions for invalid IDs.</p>
 */
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
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
