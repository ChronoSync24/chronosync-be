package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	/**
	 * Enables a user identified by id.
	 *
	 * @param id {@link Long} user id
	 * @throws ServiceException if no user with specified id is found
	 */
	@Override
	public void enable(Long id) throws ServiceException {
		UserFilterBuilder userFilterBuilder = UserFilterBuilder.builder().id(id).build();

		userRepository.findOne(userFilterBuilder.toSpecification())
			.ifPresentOrElse(user -> {
				user.setIsEnabled(true);
				userRepository.save(user);
			}, () -> {
				throw new ServiceException("User with provided id is not found.");
			});
	}
}
