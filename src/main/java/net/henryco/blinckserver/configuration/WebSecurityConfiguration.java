package net.henryco.blinckserver.configuration;

import net.henryco.blinckserver.security.auth.FacebookAuthManager;
import net.henryco.blinckserver.security.jwt.JWTAuthFilter;
import net.henryco.blinckserver.security.jwt.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Henry on 21/08/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {
		UserDetailsService.class, FacebookAuthManager.class
})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final AuthenticationManager facebookAuthManager;


	@Autowired
	public WebSecurityConfiguration(UserDetailsService userDetailsService,
									AuthenticationManager facebookAuthManager) {
		this.userDetailsService = userDetailsService;
		this.facebookAuthManager = facebookAuthManager;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers(HttpMethod.GET, "/public/**").permitAll()
				.antMatchers("/login/**").permitAll()
				.anyRequest().authenticated()
				.and()
				// We filter the api/login requests
				.addFilterBefore(
						new JWTLoginFilter("/login/**", facebookAuthManager),
						UsernamePasswordAuthenticationFilter.class
				)
				// And filter other requests to check the presence of JWT in header
				.addFilterBefore(
						new JWTAuthFilter(),
						UsernamePasswordAuthenticationFilter.class
				);



	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.and()
				.inMemoryAuthentication()
				.withUser("admin").password("password").roles("ADMIN", "USER");

	}


}
