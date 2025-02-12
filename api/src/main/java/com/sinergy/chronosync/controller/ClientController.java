package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.service.impl.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing clients.
 * <p>This controller provides endpoints for retrieving, creating, updating and deleting clients</p>
 */
@RestController
@RequestMapping(path = "api/v1/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientServiceImpl clientService;

	/** java docs*/
	@PostMapping("/get")
	public ResponseEntity<Page<Client>> getClients(
		@RequestBody BasePaginationRequest paginationRequest
	) {
		int page = paginationRequest.getPage();
		int size = paginationRequest.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Client> clients = clientService.getClients(pageRequest);
		return ResponseEntity.ok(clients);
	}

	/**
	 * Creates new client for the current user's firm
	 *
	 * @param request {@link ClientRequestDTO} containing the details of the new client.
	 * @return created {@link Client} along with an HTTP status of 201 (Created).
	 */
	@PostMapping("/create")
	public ResponseEntity<Client> createClient(
		@RequestBody ClientRequestDTO request
	) {
		Client client = clientService.createClient(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(client);
	}

	/**
	 * Updates an existing client identified by its ID, or creates a new one if no ID is provided.
	 *
	 * @param request {@link ClientRequestDTO} containing details of the client.
	 * @return {@link ResponseEntity} containing the updated or created {@link Client}.
	 */
	@PutMapping
	public ResponseEntity<Client> updateClient(
		@RequestBody ClientRequestDTO request
	) {
		Client updatedClient = clientService.updateClient(request);
		return ResponseEntity.ok(updatedClient);
	}

	/**
	 * Deletes a client by its ID.
	 *
	 * @param id {@link Long} ID of the client to delete.
	 */
	@DeleteMapping
	public ResponseEntity<Client> deleteClient(
		@RequestParam Long id
	) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}
}
