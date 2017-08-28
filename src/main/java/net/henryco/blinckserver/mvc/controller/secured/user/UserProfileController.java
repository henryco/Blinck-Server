package net.henryco.blinckserver.mvc.controller.secured.user;

import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 26/08/17.
 */
@RestController
@RequestMapping("/protected/user")
public class UserProfileController implements BlinckProfileController {


	public @RequestMapping(
			method = GET,
			value = "/profile"
	) @Override String profile() {
		return "wellcome user";
	}


	public @RequestMapping(
			method = GET,
			value = "/permissions"
	) @Override String[] permissions(Authentication authentication) {

		return authentication
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList())
		.toArray(new String[0]);
	}

}