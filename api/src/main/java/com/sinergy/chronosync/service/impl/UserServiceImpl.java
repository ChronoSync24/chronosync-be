package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.dto.request.UserCreateRequestDTO;
import com.sinergy.chronosync.dto.response.UserCreateResponseDTO;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Creates new user.
	 *
	 * @param request {@link UserCreateRequestDTO} user create request
	 * @return {@link UserCreateResponseDTO} user create response
	 */
	@Override
	public UserCreateResponseDTO create(UserCreateRequestDTO request) {
		User user = request.toModel();
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		User newUser = userRepository.save(user);

		return new UserCreateResponseDTO(newUser.getId(), newUser.getUsername());
	}
}
