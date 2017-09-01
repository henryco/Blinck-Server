package net.henryco.blinckserver.mvc.controller.secured;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author Henry on 28/08/17.
 */
public interface BlinckProfileController {

	String ROLE_ADMIN = "ROLE_ADMIN";
	String ROLE_MODERATOR = "ROLE_MODERATOR";


	default @RequestMapping(
			method = GET,
			value = "/profile"
	) String profile() {
		return "wellcome in your profile page";
	}


	default @RequestMapping(
			method = GET,
			value = "/permissions"
	) String[] permissions(Authentication authentication) {

		return authentication
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
		.toArray(String[]::new);
	}


	default void rolesRequired(Authentication authentication,
							   String ... authorities) {
		for (String auth: authorities)
			if (authentication.getAuthorities().stream().noneMatch(sga -> sga.getAuthority().equals(auth)))
				throw new AccessDeniedException(auth+" required");
	}
}