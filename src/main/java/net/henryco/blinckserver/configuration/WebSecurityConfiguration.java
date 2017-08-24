package net.henryco.blinckserver.configuration;

import net.henryco.blinckserver.security.auth.FacebookAuthManager;
import net.henryco.blinckserver.security.jwt.credentials.LoginAdminCredentials;
import net.henryco.blinckserver.security.jwt.credentials.LoginFacebookCredentials;
import net.henryco.blinckserver.security.jwt.filter.JWTAuthFilter;
import net.henryco.blinckserver.security.jwt.filter.JWTLoginFilter;
import net.henryco.blinckserver.security.jwt.filter.ResetFilter;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Henry on 21/08/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {
		UserDetailsService.class,
		FacebookAuthManager.class
})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Qualifier("UserTokenAuthService")
	private final TokenAuthenticationService userTokenAuthService;

	@Qualifier("AdminTokenAuthService")
	private final TokenAuthenticationService adminTokenAuthService;


	private final UserDetailsService userDetailsService;
	private final AuthenticationManager facebookAuthManager;



	@Autowired
	public WebSecurityConfiguration(UserDetailsService userDetailsService,
									AuthenticationManager facebookAuthManager,
									TokenAuthenticationService userTokenAuthService,
									TokenAuthenticationService adminTokenAuthService) {

		this.userDetailsService = userDetailsService;
		this.facebookAuthManager = facebookAuthManager;
		this.userTokenAuthService = userTokenAuthService;
		this.adminTokenAuthService = adminTokenAuthService;
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
						new JWTLoginFilter(
								"/login/**",
								facebookAuthManager,
								userTokenAuthService,
								LoginFacebookCredentials.class
						),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore(
						new JWTLoginFilter(
								"/admin/login/**",
								authenticationManager(),
								adminTokenAuthService,
								LoginAdminCredentials.class
						),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore(
						new ResetFilter(),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore(
						new JWTAuthFilter("/admin/panel/**", adminTokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore( // And filter other requests to check the presence of JWT in header
						new JWTAuthFilter(userTokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				);
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.and()
				.inMemoryAuthentication()
				.withUser("21371488420")
				.password("password")
		.roles("ADMIN", "USER");
	}


}