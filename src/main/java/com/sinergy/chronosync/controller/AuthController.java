package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.AuthenticationRequestDTO;
import com.sinergy.chronosync.dto.request.UserRegisterRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
import com.sinergy.chronosync.dto.response.UserRegisterResponseDTO;
import com.sinergy.chronosync.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller class.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;

	/**
	 * Registers new user.
	 *
	 * @param request {@link UserRegisterRequestDTO} user register request
	 * @return {@link ResponseEntity<UserRegisterResponseDTO>} user registration response
	 */
	@PostMapping("/register")
	public ResponseEntity<UserRegisterResponseDTO> register(
		@RequestBody UserRegisterRequestDTO request
	) {
		return ResponseEntity.ok(authenticationService.register(request));
	}

	/**
	 * Authenticates user with provided credentials.
	 *
	 * @param request {@link AuthenticationRequestDTO} authentication request with credentials
	 * @return {@link ResponseEntity<AuthenticationResponse>} authentication response with JWT
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
		@RequestBody AuthenticationRequestDTO request
	) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}
