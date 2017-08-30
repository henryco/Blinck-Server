package net.henryco.blinckserver.component;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.security.details.BlinckDetailsProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Henry on 30/08/17.
 */
@Configuration
public class DetailsServiceBeans {



	public @Bean UserDetailsService profileDetailsServiceUser(
			UserAuthProfileDao authProfileDao) {

		return new BlinckDetailsProfileService<>(
				authProfileDao,
				Long::decode
		);
	}


	public @Bean UserDetailsService profileDetailsServiceAdmin(
			AdminAuthProfileDao authProfileDao) {

		return new BlinckDetailsProfileService<>(
				authProfileDao,
				Long::decode
		);
	}

}