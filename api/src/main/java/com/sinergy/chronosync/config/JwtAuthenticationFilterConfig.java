package com.sinergy.chronosync.config;

import com.sinergy.chronosync.builder.TokenFilterBuilder;
import com.sinergy.chronosync.repository.TokenRepository;
import com.sinergy.chronosync.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Class that represents JSON Web Token Authentication Filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilterConfig extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	/**
	 * Filters incoming requests to authenticate users based on JWT tokens.
	 * </br> </br>
	 * <i>Extracts the JWT from the Authorization header, validates it,
	 * and sets the authentication in the security context if valid.</i>
	 *
	 * @param request     {@link HttpServletRequest} HTTP request
	 * @param response    {@link HttpServletResponse} HTTP response
	 * @param filterChain {@link FilterChain} filter chain to continue processing
	 * @throws ServletException {@link ServletException} if a servlet-related error occurs
	 * @throws IOException      {@link IOException} if an I/O error occurs
	 */
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		username = jwtUtils.extractUsername(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean isTokenValid = tokenRepository
				.findOne(TokenFilterBuilder.builder().jwtString(jwt).build().toSpecification())
				.map(t -> jwtUtils.isTokenValid(t.jwtString, userDetails))
				.orElse(false);

			if (jwtUtils.isTokenValid(jwt, userDetails) && isTokenValid) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
				);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
