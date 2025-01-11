import { LoginRequestDTO } from '../dtos/requests/auth/LoginRequestDTO';
import { RegisterRequestDTO } from '../dtos/requests/auth/RegisterRequestDTO';
import { RegisterResponseDTO } from '../dtos/responses/auth/RegisterResponseDTO';
import { apiClient } from '../utils/ApiClient';

const ENDPOINT_PREFIX = '/auth';

/**
 * Logs in a user by sending a POST request with the provided credentials.
 * Upon successful authentication, the function stores the JWT token in the
 * localStorage to be used in subsequent authenticated requests.
 *
 * @param {LoginRequestDTO} request - The login credentials (username and password).
 * @returns {Promise<void>} - A Promise that resolves to `void`. The function doesn't return any value.
 *
 * @throws {Error} - Throws an error if the login fails. The error is logged to the console.
 */
export const login = async (request: LoginRequestDTO): Promise<void> => {
	try {
		const response = await apiClient<{ jwtString: string }>(`${ENDPOINT_PREFIX}/login`, {
			method: 'POST',
			body: request,
		});

		//TODO: This does not belong to service. Move it to login tsx file. Change docs and return of the method.
		console.log('response: ', response.jwtString);
		localStorage.setItem('token', response.jwtString);
	} catch (error) {
		throw new Error('Login failed.');
	}
};

/**
 * Registers a new user by sending a POST request with the provided registration details.
 * Upon successful registration, the function returns the server response containing
 * details about the registered user.
 *
 * @param {RegisterRequestDTO} request - The registration details
 * @returns {Promise<RegisterResponseDTO>} - A Promise that resolves to the registration response containing registered user details.
 *
 * @throws {Error} - Throws an error if the registration fails. The error is logged to the console.
 */
export const register = async (request: RegisterRequestDTO): Promise<RegisterResponseDTO> => {
	try {
		const response = await apiClient<RegisterResponseDTO>(`${ENDPOINT_PREFIX}/register`, {
			method: 'POST',
			body: request,
			headers: {
				Authorization: 'Bearer ' + localStorage.getItem('token'),
				'Content-Type': 'application/json',
			},
		});

		return response;
	} catch (error) {
		throw new Error('Registration failed.');
	}
};
