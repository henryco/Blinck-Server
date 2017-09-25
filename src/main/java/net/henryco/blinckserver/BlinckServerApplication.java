package net.henryco.blinckserver;

import net.henryco.blinckserver.configuration.MainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MainConfiguration.class})
public class BlinckServerApplication {



	public static void main(String[] args) {
		SpringApplication.run(BlinckServerApplication.class, args);
	}
}
