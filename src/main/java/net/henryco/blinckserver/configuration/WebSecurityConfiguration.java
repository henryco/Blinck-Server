package net.henryco.blinckserver.configuration;

import net.henryco.blinckserver.security.auth.FacebookAuthManager;
import net.henryco.blinckserver.security.jwt.JWTAuthFilter;
import net.henryco.blinckserver.security.jwt.JWTLoginFilter;
import net.henryco.blinckserver.security.jwt.TokenAuthService;
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
		UserDetailsService.class,
		FacebookAuthManager.class
})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final AuthenticationManager facebookAuthManager;
	private final TokenAuthService tokenAuthService;


	@Autowired
	public WebSecurityConfiguration(UserDetailsService userDetailsService,
									AuthenticationManager facebookAuthManager,
									TokenAuthService tokenAuthService) {

		this.userDetailsService = userDetailsService;
		this.facebookAuthManager = facebookAuthManager;
		this.tokenAuthService = tokenAuthService;
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
				.addFilterBefore( // We filter the api/login requests
						new JWTLoginFilter("/login/**", facebookAuthManager, tokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				)
				.addFilterBefore( // And filter other requests to check the presence of JWT in header
						new JWTAuthFilter(tokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				);
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.and()
				.inMemoryAuthentication()
				.withUser("admin")
				.password("password")
		.roles("ADMIN", "USER");
	}


}