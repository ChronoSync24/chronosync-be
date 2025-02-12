package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.ClientService;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing clients.
 * <p>This service handles all business logic related to clients, including
 * retrieving, creating, updating and deleting client entities.</p>
 */
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final UserRepository userRepository;

	/**
	 * Retrieves all clients associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link Client} objects linked to that firm's ID.
	 *
	 * @param pageRequest The pagination and sorting information.
	 * @return {@link Page} of {@link Client} objects associated with the current user's firm.
	 */
	@Override
	public Page<Client> getClients(PageRequest pageRequest) {

		Specification<Client> spec = (root, query, cb) -> {
			Join<Client, Firm> firmsJoin = root.join("firms");
			return cb.equal(firmsJoin.get("id"), getAuthUserFirm().getId());
		};

		return clientRepository.findAll(spec, pageRequest);
	}

	/**
	 * Creates a new client or updates an existing and associates it with the current user's firm.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details.
	 * @return {@link Client} representing the saved client.
	 */
	@Override
	@Transactional
	public Client createClient(ClientRequestDTO requestDto) {
		Firm authUserFirm = getAuthUserFirm();

		Optional<Client> existingClientOptional = clientRepository.findOne((root, query, cb) -> cb.and(
			cb.equal(root.get("firstName"), requestDto.getFirstName()),
			cb.equal(root.get("lastName"), requestDto.getLastName()),
			cb.equal(root.get("email"), requestDto.getEmail()),
			cb.equal(root.get("phone"), requestDto.getPhone())
		));

		if (existingClientOptional.isPresent()) {
			Client existingClient = existingClientOptional.get();
			if (!existingClient.getFirms().contains(authUserFirm)) {
				existingClient.getFirms().add(authUserFirm);
				authUserFirm.getClients().add(existingClient);
			}
			return clientRepository.save(existingClient);
		} else {
			Client client = requestDto.toModel();
			client.getFirms().add(authUserFirm);
			authUserFirm.getClients().add(client);
			return clientRepository.save(client);
		}
	}

	/**
	 * Updates an existing client or creates a new one if no ID is provided.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details.
	 * @return {@link Client} representing the updated or newly created client.
	 * @throws InvalidStateException if the client cannot be found for update.
	 */
	@Override
	public Client updateClient(ClientRequestDTO requestDto){
		Client existingClient = clientRepository.findById(requestDto.getId())
			.orElseThrow(
				()-> new InvalidStateException("Client not found")
			);

		existingClient.setFirstName(requestDto.getFirstName());
		existingClient.setLastName(requestDto.getLastName());
		existingClient.setPhone(requestDto.getPhone());
		existingClient.setEmail(requestDto.getEmail());

		return clientRepository.save(existingClient);
	}

	/**
	 * Deletes a client identified by its ID.
	 * @param id {@link Long} ID of the appointment type to delete.
	 * @throws InvalidStateException if deletion fails or the client does not exist.
	 */
	@Override
	public void deleteClient(Long id){
		if(!clientRepository.existsById(id)){
			throw new InvalidStateException("Client not found");
		}
		clientRepository.deleteById(id);
	}

	private Firm getAuthUserFirm() {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(SecurityContextHolder.getContext().getAuthentication().getName())
			.build();

		User user = userRepository.findOne(filterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		return firm;
	}
}
