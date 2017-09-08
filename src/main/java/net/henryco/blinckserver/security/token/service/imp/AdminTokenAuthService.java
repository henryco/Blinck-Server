package net.henryco.blinckserver.security.token.service.imp;

import net.henryco.blinckserver.security.token.processor.TokenAuthenticationProcessor;
import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 24/08/17.
 */
@Service @Qualifier("AdminTokenAuthService")
@PropertySource("classpath:/static/props/base.properties")
public final class AdminTokenAuthService extends TokenAuthenticationService {


	private final TokenAuthenticationProcessor processor;
	private final String token_secret;


	@Autowired
	public AdminTokenAuthService(Environment environment,
								 @Qualifier("adminTokenPostProcessor")
										 TokenAuthenticationProcessor processor) {

		this.token_secret = environment.getProperty("security.jwt.secret.admin");
		this.processor = processor;
	}

	@Override
	protected String getTokenSecret() {
		return token_secret;
	}

	@Override
	protected String getDefaultRole() {
		return "ROLE_ADMIN";
	}

	@Override
	protected TokenAuthenticationProcessor getProcessor() {
		return processor;
	}

}