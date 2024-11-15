package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller class.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * Enables user account with provided id.
	 *
	 * @param id {@link Long} user id
	 */
	@PostMapping("/enable")
	public ResponseEntity<String> enable(@RequestParam Long id) {
		userService.enable(id);
		return ResponseEntity.ok("User successfully enabled.");
	}
}
