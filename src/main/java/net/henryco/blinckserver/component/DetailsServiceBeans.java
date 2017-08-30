package net.henryco.blinckserver.component;

import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.security.details.BlinckDetailsProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
@Configuration
public class DetailsServiceBeans {


	public @Bean Function<String, Long> detailsKeyConverterLong() {
		return Long::decode;
	}


	public @Bean UserDetailsService profileDetailsServiceUser(
			UserAuthProfileDao authProfileDao,
			Function<String, Long> keyConverter) {

		return new BlinckDetailsProfileService<>(
				authProfileDao,
				keyConverter
		);
	}


	public @Bean UserDetailsService profileDetailsServiceAdmin(
			AdminAuthProfileDao authProfileDao,
			Function<String, Long> keyConverter) {

		return new BlinckDetailsProfileService<>(
				authProfileDao,
				keyConverter
		);
	}


}