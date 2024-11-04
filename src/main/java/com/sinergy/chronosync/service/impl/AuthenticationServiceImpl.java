package com.sinergy.chronosync.service.impl;

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
import com.sinergy.chronosync.service.AuthenticationService;
import com.sinergy.chronosync.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service interface implementation.
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	/**
	 * Registers new user.
	 *
	 * @param request {@link UserRegisterRequestDTO} user register request
	 * @return {@link UserRegisterResponseDTO} user register response
	 */
	@Override
	public UserRegisterResponseDTO register(UserRegisterRequestDTO request) {
		User user = request.toModel();
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User newUser = userRepository.save(user);

		return new UserRegisterResponseDTO(newUser.getId(), newUser.getUsername());
	}

	/**
	 * Authenticates provided user with username and password.
	 *
	 * @param request {@link AuthenticationRequestDTO} authentication request
	 * @return {@link AuthenticationResponse} JSON web token
	 */
	@Override
	public AuthenticationResponse authenticate(AuthenticationRequestDTO request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);

		User user = userRepository
			.findOne(UserFilterBuilder.builder().username(request.getUsername()).build().toSpecification())
			.orElseThrow(() -> new ServiceException("User authentication failed."));

		String jwt = jwtUtils.generateJWTString(user);

		Token token = tokenRepository
			.findOne(TokenFilterBuilder.builder().user(user).build().toSpecification())
			.orElseGet(() -> Token.builder().user(user).build());

		token.setJwtString(jwt);
		tokenRepository.save(token);

		return AuthenticationResponse.builder().jwtString(jwt).build();
	}
}
