package net.henryco.blinckserver.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Henry on 21/08/17.
 */
@Configuration
@Import({WebSecurityConfiguration.class})
public class MainConfiguration {


}
