package net.henryco.blinckserver.mvc.controller.secured;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author Henry on 28/08/17.
 */
public interface BlinckProfileController {


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
				.collect(Collectors.toList())
		.toArray(new String[0]);
	}

}