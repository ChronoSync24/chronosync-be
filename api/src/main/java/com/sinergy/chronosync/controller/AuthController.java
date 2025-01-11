package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.LoginRequestDTO;
import com.sinergy.chronosync.dto.response.AuthenticationResponse;
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
	 * Authenticates user with provided credentials.
	 *
	 * @param request {@link LoginRequestDTO} authentication request with credentials
	 * @return {@link ResponseEntity<AuthenticationResponse>} authentication response with JWT
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(
		@RequestBody LoginRequestDTO request
	) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}
