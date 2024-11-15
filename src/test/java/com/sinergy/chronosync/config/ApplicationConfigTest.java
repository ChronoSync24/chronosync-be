package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ApplicationConfig}.
 *
 * <p>Verifies the configuration of security-related beans
 * in the {@link ApplicationConfig} class. It ensures that the beans for
 * user details service, password encoder, authentication provider, and
 * authentication manager are correctly instantiated and configured.</p>
 *
 * <p>Mock objects are used to simulate the user repository and other dependencies,
 * allowing for focused testing of the configuration methods.</p>
 */
class ApplicationConfigTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private AuthenticationConfiguration authenticationConfiguration;

	@InjectMocks
	private ApplicationConfig applicationConfig;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the creation of the {@link UserDetailsService} bean without throwing exceptions.
	 *
	 * <p>Verifies that the user details service retrieves a user by
	 * username successfully.</p>
	 */
	@Test
	void userDetailsServiceSuccessTest() {
		User user = mock(User.class);
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));

		UserDetailsService userDetailsService = applicationConfig.userDetailsService();
		assertEquals(user, userDetailsService.loadUserByUsername("testUsername"));
	}

	/**
	 * Tests the creation of the {@link UserDetailsService} bean with exception handling.
	 *
	 * <p>Verifies that the user details service throws a {@link UsernameNotFoundException}
	 * when the user is not found.</p>
	 */
	@Test
	void userDetailsServiceUsernameNotFoundExceptionTest() {
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.empty());

		UserDetailsService userDetailsService = applicationConfig.userDetailsService();
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonExistentUser"));
	}

	/**
	 * Tests the creation of the {@link PasswordEncoder} bean.
	 *
	 * <p>Verifies that the password encoder is an instance of
	 * {@link BCryptPasswordEncoder}.</p>
	 */
	@Test
	void passwordEncoderTest() {
		PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
		assertNotNull(passwordEncoder);
	}

	/**
	 * Tests the creation of the {@link AuthenticationProvider} bean.
	 *
	 * <p>Verifies that the authentication provider is configured
	 * with the user details service and password encoder.</p>
	 */
	@Test
	void authenticationProviderTest() {
		AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
		assertNotNull(authenticationProvider);
	}

	/**
	 * Tests the creation of the {@link AuthenticationManager} bean.
	 *
	 * <p>Verifies that the authentication manager is created
	 * successfully from the provided {@link AuthenticationConfiguration}.</p>
	 *
	 * @throws Exception if an error occurs during the retrieval of the authentication manager
	 */
	@Test
	void authenticationManagerTest() throws Exception {
		AuthenticationManager mockManager = mock(AuthenticationManager.class);
		when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockManager);

		AuthenticationManager authenticationManager =
			applicationConfig.authenticationManager(authenticationConfiguration);

		assertNotNull(authenticationManager);
	}
}

