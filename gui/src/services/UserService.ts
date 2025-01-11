import { apiClient } from '../utils/ApiClient';
import { UserRequestDTO } from '../dtos/requests/UserRequestDTO';
import { User } from '../models/user/User';

const ENDPOINT_PREFIX = '/user';

/**
 * Creates a new user.
 *
 * @param {UserRequestDTO} request - User creation request
 * @returns {Promise<User>} - Promise that resolves to the created user object.
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const create = async (request: UserRequestDTO): Promise<User> => {
	try {
		const response = await apiClient<User>(`${ENDPOINT_PREFIX}/create`, {
			method: 'POST',
			body: request,
			headers: {
				Authorization: 'Bearer ' + localStorage.getItem('token'),
				'Content-Type': 'application/json',
			},
		});

		return response;
	} catch (error) {
		throw new Error('User creation failed.');
	}
};
