package net.henryco.blinckserver.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Henry on 21/08/17.
 */
@Configuration
//@ComponentScan(basePackageClasses = AuthUserService.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

}
