package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.AppointmentTypeRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.appointmentType.AppointmentType;
import com.sinergy.chronosync.model.firm.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.AppointmentTypeRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.AppointmentTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AppointmentTypeService.
 */
class AppointmentTypeServiceTest {

	@Mock
	private AppointmentTypeRepository appointmentTypeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AppointmentTypeServiceImpl appointmentTypeService;

	/**
	 * Sets up the mock dependencies before each test.
	 */
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("testUser");
		SecurityContextHolder.setContext(securityContext);
	}

	private User assignFirmToUser(String username, Long id){
		User mockUser = new User();
		mockUser.setUsername(username);
		Firm mockFirm = new Firm();
		mockFirm.setId(id);
		mockUser.setFirm(mockFirm);
		return mockUser;
	}

	/**
	 * Tests the getUserAppointmentTypes method when the user is valid.
	 *
	 * @throws Exception if any error occurs during the test
	 */
	@Test
	void getUserAppointmentTypesValidUserReturnsPageOfAppointmentTypes() {
		when(userRepository.findOne(Mockito.any(Specification.class)))
			.thenReturn(Optional.of(assignFirmToUser("testUser", 1L)));

		AppointmentType mockAppointmentType = new AppointmentType();
		mockAppointmentType.setName("Test Appointment Type");
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<AppointmentType> appointmentTypes = new PageImpl<>(List.of(mockAppointmentType));

		when(appointmentTypeRepository.findAll(Mockito.any(Specification.class), eq(pageRequest)))
			.thenReturn(appointmentTypes);

		Page<AppointmentType> result = appointmentTypeService.getUserAppointmentTypes(pageRequest);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("Test Appointment Type", result.getContent().get(0).getName());

		verify(userRepository, times(1)).findOne(Mockito.any(Specification.class));
		verify(appointmentTypeRepository, times(1)).findAll(Mockito.any(Specification.class),
			eq(pageRequest));
	}

	/**
	 * Tests the getUserAppointmentTypes method when the user is not found.
	 *
	 * @throws Exception if any error occurs during the test
	 */
	@Test
	void getUserAppointmentTypesUserNotFoundThrowsException() {
		when(userRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.empty());
		PageRequest pageRequest = PageRequest.of(0, 10);
		UserNotFoundException thrownException = assertThrows(UserNotFoundException.class, () -> {
			appointmentTypeService.getUserAppointmentTypes(pageRequest);
		});

		assertEquals("User not found", thrownException.getMessage());

		verify(userRepository, times(1)).findOne(Mockito.any(Specification.class));
		verify(appointmentTypeRepository, never()).findAll(Mockito.any(Specification.class),
			Mockito.any(Pageable.class));
	}

	/**
	 * Tests the createAppointmentType method when the request is valid.
	 *
	 * @throws Exception if any error occurs during the test
	 */
	@Test
	void createAppointmentTypeValidRequestCreatesAppointmentType() {

		when(userRepository.findOne(Mockito.any(Specification.class))).thenReturn(
			Optional.of(assignFirmToUser("testUser", 1L)));

		AppointmentTypeRequestDTO requestDto = new AppointmentTypeRequestDTO();
		requestDto.setName("New Appointment");
		requestDto.setDurationMinutes(30);
		requestDto.setPrice(100.0);

		AppointmentType mockAppointmentType = new AppointmentType();
		mockAppointmentType.setName(requestDto.getName());
		mockAppointmentType.setDurationMinutes(requestDto.getDurationMinutes());
		mockAppointmentType.setPrice(requestDto.getPrice());
		when(appointmentTypeRepository.save(Mockito.any(AppointmentType.class))).thenReturn(mockAppointmentType);

		AppointmentType createdAppointmentType = appointmentTypeService.createAppointmentType(requestDto);

		assertNotNull(createdAppointmentType);
		assertEquals("New Appointment", createdAppointmentType.getName());
		assertEquals(30, createdAppointmentType.getDurationMinutes());
		assertEquals(100.0, createdAppointmentType.getPrice());

		verify(userRepository, times(1)).findOne(Mockito.any(Specification.class));
		verify(appointmentTypeRepository, times(1)).save(Mockito.any(AppointmentType.class));
	}

	/**
	 * Tests the deleteAppointmentType method when the appointment type exists.
	 */
	@Test
	void deleteAppointmentTypeValidIdDeletesAppointmentType() {
		Long appointmentTypeId = 1L;

		when(appointmentTypeRepository.existsById(appointmentTypeId)).thenReturn(true);

		appointmentTypeService.deleteAppointmentType(appointmentTypeId);

		verify(appointmentTypeRepository, times(1)).deleteById(appointmentTypeId);
	}

	/**
	 * Tests the deleteAppointmentType method when the appointment type does not exist.
	 */
	@Test
	void deleteAppointmentTypeInvalidIdThrowsException() {
		Long appointmentTypeId = 1L;

		when(appointmentTypeRepository.existsById(appointmentTypeId)).thenReturn(false);

		InvalidStateException thrownException = assertThrows(InvalidStateException.class, () -> {
			appointmentTypeService.deleteAppointmentType(appointmentTypeId);
		});

		assertEquals("Appointment type does not exist.", thrownException.getMessage());

		verify(appointmentTypeRepository, never()).deleteById(appointmentTypeId);
	}

	/**
	 * Tests the updateAppointmentType method with valid data.
	 */
	@Test
	void updateAppointmentTypeValidRequestUpdatesAppointmentType() {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("testUser");
		SecurityContextHolder.setContext(securityContext);

		Firm mockFirm = new Firm();
		mockFirm.setId(1L);
		User mockUser = new User();
		mockUser.setUsername("testUser");
		mockUser.setFirm(mockFirm);
		when(userRepository.findOne(Mockito.any(Specification.class)))
			.thenReturn(Optional.of(mockUser));

		Long appointmentTypeId = 1L;
		AppointmentTypeRequestDTO requestDto = new AppointmentTypeRequestDTO();
		requestDto.setId(appointmentTypeId);
		requestDto.setName("updatedName");
		requestDto.setDurationMinutes(60);
		requestDto.setPrice(200.0);

		AppointmentType existingAppointmentType = new AppointmentType();
		existingAppointmentType.setId(appointmentTypeId);
		existingAppointmentType.setName("oldName");
		existingAppointmentType.setDurationMinutes(30);
		existingAppointmentType.setPrice(100.0);
		existingAppointmentType.setFirm(mockFirm);

		when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(existingAppointmentType));
		when(appointmentTypeRepository.save(Mockito.any(AppointmentType.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		AppointmentType updatedAppointmentType = appointmentTypeService.updateAppointmentType(requestDto);

		assertNotNull(updatedAppointmentType);
		assertEquals("updatedName", updatedAppointmentType.getName());
		assertEquals(60, updatedAppointmentType.getDurationMinutes());
		assertEquals(200.0, updatedAppointmentType.getPrice());

		verify(userRepository, times(1)).findOne(Mockito.any(Specification.class));
		verify(appointmentTypeRepository, times(1)).findById(appointmentTypeId);
		verify(appointmentTypeRepository, times(1)).save(Mockito.any(AppointmentType.class));
	}
}
