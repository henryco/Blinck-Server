package net.henryco.blinckserver;

import net.henryco.blinckserver.configuration.MainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MainConfiguration.class})
public class BlinckServerApplication {

	public static final String[] FACEBOOK_PERMISSIONS = {

			"user_about_me",
			"user_birthday",
			"user_education_history",
			"user_friends",
			"user_likes",
			"user_location",
			"user_photos",
			"read_custom_friendlists"
	};


	public static void main(String[] args) {
		SpringApplication.run(BlinckServerApplication.class, args);
	}
}
