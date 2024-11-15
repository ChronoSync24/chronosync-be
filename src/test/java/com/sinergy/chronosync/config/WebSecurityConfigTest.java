package com.sinergy.chronosync.config;

import com.sinergy.chronosync.model.Token;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.model.user.UserRole;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link WebSecurityConfig}.
 *
 * <p>Verifies the access control behavior of different user roles to various endpoints
 * in the application. The tests focus on ensuring that only authorized users can
 * access protected resources, with role-based access checks for endpoints such as
 * {@code /api/v1/user/enable}.</p>
 *
 * <p>Mock objects are used to simulate the behavior of the user repository, token repository,
 * and user details service, allowing for isolated testing of security configurations and access control.</p>
 */
@SpringBootTest
public class WebSecurityConfigTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private TokenRepository tokenRepository;

	@MockBean
	private UserDetailsService userDetailsService;

	private MockMvc mockMvc;

	/**
	 * Sets up the test environment by initializing the MockMvc object and applying security filters.
	 * This method is executed before each test to ensure a fresh setup for every test case.
	 */
	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.apply(springSecurity(springSecurityFilterChain))
			.build();
	}

	/**
	 * Tests the access control for the {@code /api/v1/user/enable} endpoint when an EMPLOYEE user
	 * attempts to access it. The test expects a 403 Forbidden status, indicating that the user
	 * does not have permission to access this endpoint.
	 *
	 * @throws Exception if an error occurs during the execution of the test
	 */
	@Test
	public void employeeEndpointAccessDeniedTest() throws Exception {
		User user = generateUser(UserRole.EMPLOYEE);
		Token token = generateUserToken(user);

		when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(token));

		mockMvc.perform(post("/api/v1/user/enable")
				.header("Authorization", "Bearer " + token.jwtString)
				.param("id", "1"))
			.andExpect(status().isForbidden());
	}

	/**
	 * Tests the access control for the {@code /api/v1/user/enable} endpoint when an ADMINISTRATOR user
	 * accesses it. The test expects a 200 OK status, indicating that the user has the necessary
	 * permissions to access this endpoint.
	 *
	 * @throws Exception if an error occurs during the execution of the test
	 */
	@Test
	public void administratorEndpointAccessTest() throws Exception {
		User user = generateUser(UserRole.ADMINISTRATOR);
		Token token = generateUserToken(user);

		when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(new User()));
		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(token));

		mockMvc.perform(post("/api/v1/user/enable")
				.header("Authorization", "Bearer " + token.jwtString)
				.param("id", "1"))
			.andExpect(status().isOk());
	}

	/**
	 * Tests the access control for the {@code /api/v1/user/employees} endpoint when a MANAGER user
	 * accesses it. The test expects a 200 OK status, assuming that the correct implementation
	 * of the manager endpoint will be used in the future.
	 *
	 * <p>Note: The actual endpoint and functionality for manager access are yet to be implemented,
	 * and the test is currently commented out for further implementation.</p>
	 *
	 * @throws Exception if an error occurs during the execution of the test
	 */
//	@Test
//	public void managerEndpointAccessTest() throws Exception {
//		User user = generateUser(UserRole.MANAGER);
//		Token token = generateUserToken(user);
//
//		when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
//		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(new User()));
//		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(token));
//
//		//TODO: Change to correct implementation of manager endpoint
//		mockMvc.perform(post("/api/v1/user/employees")
//				.header("Authorization", "Bearer " + token.jwtString)
//				.param("id", "1"))
//			.andExpect(status().isOk());
//	}

	/**
	 * Tests the access control for the {@code /api/v1/client} endpoint when an EMPLOYEE user
	 * accesses it. The test expects a 200 OK status, assuming that the correct implementation
	 * of the employee endpoint will be used in the future.
	 *
	 * <p>Note: The actual endpoint and functionality for employee access are yet to be implemented,
	 * and the test is currently commented out for further implementation.</p>
	 *
	 * @throws Exception if an error occurs during the execution of the test
	 */
//	@Test
//	public void employeeEndpointAccessTest() throws Exception {
//		User user = generateUser(UserRole.EMPLOYEE);
//		Token token = generateUserToken(user);
//
//		when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
//		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(new User()));
//		when(tokenRepository.findOne(Mockito.<Specification<Token>>any())).thenReturn(Optional.of(token));
//
//		//TODO: Change to correct implementation of employee endpoint
//		mockMvc.perform(post("/api/v1/client")
//				.header("Authorization", "Bearer " + token.jwtString)
//				.param("id", "1"))
//			.andExpect(status().isOk());
//	}

	/**
	 * Helper method to generate a {@link User} object with the specified role.
	 *
	 * @param role {@link UserRole} role to assign to the User
	 * @return {@link User} user object with the specified role
	 */
	private User generateUser(UserRole role) {
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testPassword");
		user.setRole(role);
		user.setIsEnabled(true);
		user.setIsLocked(false);

		return user;
	}

	/**
	 * Helper method to generate a {@link Token} object for the specified user.
	 *
	 * @param user {@link User} user object for which to generate the Token
	 * @return {@link Token} token object containing the JWT string for the user
	 */
	private Token generateUserToken(User user) {
		return Token.builder()
			.id(1)
			.user(user)
			.jwtString(jwtUtils.generateJWTString(user))
			.build();
	}
}
