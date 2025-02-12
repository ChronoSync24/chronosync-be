package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Client service interface class
 */
public interface ClientService {

	/**
	 * Retrieves a paginated list of clients associated with the user's firm.
	 *
	 * @return {@link Page} of {@link Client} containing all clients for the user's firm.
	 */
	Page<Client> getClients(PageRequest pageRequest);

	/**
	 * Creates a new client and stores it in the database.
	 * @param requestDto {@link ClientRequestDTO} containing client details
	 * @return {@link Client} representing the saved client.
	 */
	Client createClient(ClientRequestDTO requestDto);

	/**
	 * Updates an existing client identified by its ID.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing updated details.
	 * @return {@link Client} representing the updated client.
	 */
	Client updateClient(ClientRequestDTO requestDto);

	/**
	 * Deletes a client by its ID.
	 *
	 * @param id {@link Long} ID of the client to delete.
	 */
	void deleteClient(Long id);
}
