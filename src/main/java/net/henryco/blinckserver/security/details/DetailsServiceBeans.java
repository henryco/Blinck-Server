package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.configuration.BeansConfiguration;
import net.henryco.blinckserver.mvc.model.dao.security.AdminAuthProfileDao;
import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
@Configuration
public class DetailsServiceBeans extends BeansConfiguration {


	public @Bean Function<String, Long>
	stringToLongConverter() {
		return Long::decode;
	}


	public @Bean Function<String, String>
	stringToStringConverter() {
		return String::new;
	}


	public @Bean Function<Long, String>
	longToStringConverter() {
		return String::valueOf;
	}



	public @Bean UserDetailsService
	profileDetailsServiceUser(UserAuthProfileDao authProfileDao,
							  Function<String, Long> keyConverter,
							  Function<Long, String> keyDeConverter) {
		return new BlinckDetailsProfileService<>(
				authProfileDao,
				keyConverter,
				keyDeConverter
		);
	}


	public @Bean UserDetailsService
	profileDetailsServiceAdmin(AdminAuthProfileDao authProfileDao,
							   Function<String, String> keyConverter) {
		return new BlinckDetailsProfileService<>(
				authProfileDao,
				keyConverter,
				keyConverter
		);
	}


}