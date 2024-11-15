package com.sinergy.chronosync.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link JwtUtils} class.
 *
 * <p>This class provides unit tests for the functionality of the {@link JwtUtils} utility,
 * which includes generating, validating, and extracting information from JSON Web Tokens (JWT).
 * It uses the Mockito framework to mock dependencies and validate JWT operations.</p>
 */
class JwtUtilsTest {

	@Mock
	private UserDetails mockUserDetails;

	@InjectMocks
	private JwtUtils jwtUtils;

	/**
	 * Sets up the test environment before each test case.
	 *
	 * <p>Initializes the mocked objects and sets the necessary fields in the {@link JwtUtils} instance
	 * using reflection. The secret key and expiration time for JWTs are configured for testing.</p>
	 */
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(
			jwtUtils,
			"SECRET_KEY",
			"dfaEsadgasdawRayty523sddsaedGVzdFNlY3JldEtleU1vY2s=" // Base64 encoded secret key for testing
		);
		ReflectionTestUtils.setField(jwtUtils, "JWT_EXPIRATION", 1L);
		when(mockUserDetails.getUsername()).thenReturn("testuser");
	}

	/**
	 * Tests the {@link JwtUtils#generateJWTString(UserDetails)} method.
	 *
	 * <p>Verifies that a valid JWT is generated from the provided {@link UserDetails}.
	 * It checks that the token is not null and that the extracted username from the token
	 * matches the expected username.</p>
	 */
	@Test
	void generateJwtTest() {
		String jwtToken = jwtUtils.generateJWTString(mockUserDetails);
		assertNotNull(jwtToken);

		String extractedUsername = jwtUtils.extractUsername(jwtToken);
		assertEquals("testuser", extractedUsername);
	}

	/**
	 * Tests the {@link JwtUtils#extractUsername(String)} method.
	 *
	 * <p>Verifies that the correct username is extracted from a generated JWT. This test
	 * ensures that the method properly retrieves the username (subject) embedded in the token.</p>
	 */
	@Test
	void extractUsernameTest() {
		String jwtToken = jwtUtils.generateJWTString(mockUserDetails);
		String username = jwtUtils.extractUsername(jwtToken);

		assertEquals("testuser", username);
	}

	/**
	 * Tests the {@link JwtUtils#isTokenValid(String, UserDetails)} method.
	 *
	 * <p>Validates that a generated JWT is considered valid when compared with the original
	 * {@link UserDetails}. This test confirms that the token is not expired and that the
	 * username in the token matches the expected username.</p>
	 */
	@Test
	void isTokenValidTest() {
		String jwtToken = jwtUtils.generateJWTString(mockUserDetails);

		assertTrue(jwtUtils.isTokenValid(jwtToken, mockUserDetails));
	}
}
