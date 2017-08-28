package net.henryco.blinckserver.mvc.service.security;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 24/08/17.
 */
@Service @Qualifier("AdminTokenAuthService")
@PropertySource("classpath:/static/props/base.properties")
public final class AdminTokenAuthService extends TokenAuthenticationService {

	private final UserDetailsService detailsService;
	private final String token_secret;

	@Autowired
	public AdminTokenAuthService(UserDetailsService detailsService, Environment environment) {
		this.token_secret = environment.getProperty("security.jwt.secret.admin");
		this.detailsService = detailsService;
	}

	@Override
	protected String getTokenSecret() {
		return token_secret;
	}

	@Override
	protected UserDetailsService getUserDetailsService() {
		return detailsService;
	}

	@Override
	protected String getDefaultRole() {
		return "ROLE_ADMIN";
	}
}