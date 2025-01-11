const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

if (!API_BASE_URL) {
	throw new Error('REACT_APP_API_BASE_URL is not defined in the environment variables.');
}

export interface ApiRequestOptions extends Omit<RequestInit, 'body'> {
	body?: Record<string, any> | string;
}

const getAuthToken = (): string | null => {
	return localStorage.getItem('authToken');
};

export const apiClient = async <T>(endpoint: string, { body, ...options }: ApiRequestOptions = {}): Promise<T> => {
	const url = `${API_BASE_URL}${endpoint}`;

	const token = getAuthToken();

	const headers = {
		'Content-Type': 'application/json',
		...(token && { Authorization: `Bearer ${token}` }),
		...options.headers,
	};

	const response = await fetch(url, {
		...options,
		headers,
		body: body ? (typeof body === 'string' ? body : JSON.stringify(body)) : undefined,
	});

	if (!response.ok) {
		const error = await response.text();
		throw new Error(`API Error: ${response.status} - ${error}`);
	}

	return response.json();
};