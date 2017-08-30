package net.henryco.blinckserver.configuration;

import net.henryco.blinckserver.security.auth.FacebookAuthManager;
import net.henryco.blinckserver.security.jwt.credentials.LoginAdminCredentials;
import net.henryco.blinckserver.security.jwt.credentials.LoginFacebookCredentials;
import net.henryco.blinckserver.security.jwt.filter.JWTAuthFilter;
import net.henryco.blinckserver.security.jwt.filter.JWTLoginFilter;
import net.henryco.blinckserver.security.jwt.filter.JWTResetFilter;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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

@PropertySource("classpath:/static/props/base.properties")
@Configuration @ComponentScan(basePackageClasses = {
		FacebookAuthManager.class
}) public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


	private final TokenAuthenticationService userTokenAuthService;
	private final TokenAuthenticationService adminTokenAuthService;

	private final UserDetailsService userDetailsService;
	private final AuthenticationManager facebookAuthManager;



	@Autowired
	public WebSecurityConfiguration(@Qualifier("profileDetailsServiceAdmin") UserDetailsService userDetailsService,
									@Qualifier("facebookAuthManager") AuthenticationManager facebookAuthManager,
									@Qualifier("UserTokenAuthService") TokenAuthenticationService userTokenAuthService,
									@Qualifier("AdminTokenAuthService") TokenAuthenticationService adminTokenAuthService
	) {
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

				.addFilterBefore( // We filter the api/ USER login requests
						new JWTLoginFilter(
								"/login/user/**",
								facebookAuthManager,
								userTokenAuthService,
								LoginFacebookCredentials.class
						),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore( // We filter the api/ ADMIN login requests
						new JWTLoginFilter(
								"/login/admin/**",
								authenticationManager(),
								adminTokenAuthService,
								LoginAdminCredentials.class
						),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore( // Reset all of your Privileges
						new JWTResetFilter(),
						UsernamePasswordAuthenticationFilter.class
				)


				.addFilterBefore( // And filter requests to check the presence of JWT in header
						new JWTAuthFilter(userTokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore( // Check your admin Privileges
						new JWTAuthFilter(
								"/protected/admin/**",
								adminTokenAuthService
						),
						UsernamePasswordAuthenticationFilter.class
				)

		;
	}



	private @Value("${security.default.admin.name}") String admin_name;
	private @Value("${security.default.admin.password}") String admin_pass;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.and()
				.inMemoryAuthentication()
				.withUser(admin_name)
				.password(admin_pass)
		.roles("ADMIN", "USER");
	}


}