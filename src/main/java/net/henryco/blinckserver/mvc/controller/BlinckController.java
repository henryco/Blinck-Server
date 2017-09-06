package net.henryco.blinckserver.mvc.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

/**
 * @author Henry on 04/09/17.
 */
public interface BlinckController {

	String JSON = "application/json; charset=UTF-8";
	String ROLE_MODERATOR = "ROLE_MODERATOR";
	String ROLE_ADMIN = "ROLE_ADMIN";
	String ROLE_USER = "ROLE_USER";

	default void rolesRequired(Authentication authentication,
							   String ... authorities) {
		for (String auth: authorities)
			if (authentication.getAuthorities().stream().noneMatch(sga -> sga.getAuthority().equals(auth)))
				throw new AccessDeniedException(auth+" required");
	}

	default Long longID(Authentication authentication) {
		return Long.decode(authentication.getName());
	}
}