package com.sinergy.chronosync.service;

import org.hibernate.service.spi.ServiceException;

/**
 * User service interface class.
 */
public interface UserService {

	/**
	 * Enables user account with provided id.
	 *
	 * @param id {@link Long} user id
	 */
	void enable(Long id) throws ServiceException;
}
