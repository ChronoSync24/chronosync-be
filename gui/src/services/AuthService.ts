import { LoginRequestDTO } from '../dtos/requests/LoginRequestDTO';
import { apiClient } from '../utils/ApiClient';

const ENDPOINT_PREFIX = '/auth';

//TODO: Setting token and removing token from the local storage does not belong to the service. Return the jwt string, and move logic to login tsx file. Change docs and return of the method.

/**
 * Logs in a user with the provided credentials.
 *
 * @param {LoginRequestDTO} request - The login credentials (username and password).
 * @returns {Promise<void>} - A Promise that resolves to `void`. The function doesn't return any value.
 *
 * @throws {Error} - Throws an error if the login fails.
 */
export const login = async (request: LoginRequestDTO): Promise<void> => {
	try {
		const response = await apiClient<{ jwtString: string }>(`${ENDPOINT_PREFIX}/login`, {
			method: 'POST',
			body: request,
		});

		localStorage.setItem('token', response.jwtString);
	} catch (error) {
		throw new Error('Login failed.');
	}
};

/**
 * Logs out user.
 *
 * @throws {Error} - Throws an error if the logout fails.
 */
export const logout = async (): Promise<void> => {
	try {
		await apiClient<{ jwtString: string }>(`${ENDPOINT_PREFIX}/login`, {
			method: 'GET',
			headers: {
				Authorization: 'Bearer ' + localStorage.getItem('token'),
				'Content-Type': 'application/json',
			},
		});

		localStorage.removeItem('token');
	} catch (error) {
		throw new Error('Logout failed.');
	}
};
