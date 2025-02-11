package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.model.client.Client;
import com.sinergy.chronosync.model.firm.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientServiceImpl clientService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Firm firm = new Firm();
        firm.setId(1L);

        User user = new User();
        user.setUsername("testUser");
        user.setFirm(firm);
        when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));
    }
    /*
    @Test
    void getClientsTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("123456789");
        Firm firm = new Firm();
        firm.setId(1L);
        client.getFirms().add(firm);
        Page<Client> clients = new PageImpl<>(List.of(client));

        when(clientRepository.findAll(any(PageRequest.class))).thenReturn(clients);

        Page<Client> result = clientService.getClients(pageRequest);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "Should contain 1 client");
        assertEquals("John", result.getContent().getFirst().getFirstName());

        verify(clientRepository, times(1)).findAll(any(PageRequest.class));
    }
    */
    @Test
    void createClientTest() {
        Firm firm = new Firm();
        firm.setId(1L);

        User user = new User();
        user.setFirm(firm);

        ClientRequestDTO requestDto = ClientRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .build();

        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("123456789");

        when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(requestDto);

        assertNotNull(createdClient);
        assertEquals("John", createdClient.getFirstName());
        assertEquals("Doe", createdClient.getLastName());
        assertEquals("john.doe@example.com", createdClient.getEmail());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClientWhenClientExistsTest() {
        Firm firm = new Firm();
        firm.setId(1L);

        User user = new User();
        user.setFirm(firm);

        ClientRequestDTO requestDto = ClientRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .build();

        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setFirstName("John");
        existingClient.setLastName("Doe");
        existingClient.setEmail("john.doe@example.com");
        existingClient.setPhone("123456789");

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(clientRepository.findById(any())).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        Client updatedClient = clientService.createClient(requestDto);

        assertNotNull(updatedClient);
        assertEquals("John", updatedClient.getFirstName());
        assertEquals("Doe", updatedClient.getLastName());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClientTest() {
        ClientRequestDTO requestDto = ClientRequestDTO.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("987654321")
                .build();

        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setFirstName("John");
        existingClient.setLastName("Doe");
        existingClient.setEmail("john.doe@example.com");
        existingClient.setPhone("123456789");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        Client updatedClient = clientService.updateClient(requestDto);

        assertNotNull(updatedClient);
        assertEquals("Jane", updatedClient.getFirstName());
        assertEquals("Doe", updatedClient.getLastName());
        assertEquals("jane.doe@example.com", updatedClient.getEmail());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClientNotFoundTest() {
        ClientRequestDTO requestDto = ClientRequestDTO.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("987654321")
                .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidStateException thrownException = assertThrows(
                InvalidStateException.class,
                () -> clientService.updateClient(requestDto)
        );

        assertEquals("Client not found", thrownException.getMessage());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void deleteClientTest() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(true);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void deleteClientNotFoundTest() {
        Long clientId = 1L;

        when(clientRepository.existsById(clientId)).thenReturn(false);

        InvalidStateException thrownException = assertThrows(
                InvalidStateException.class,
                () -> clientService.deleteClient(clientId)
        );

        assertEquals("Client not found", thrownException.getMessage());
        verify(clientRepository, never()).deleteById(clientId);
    }
}
