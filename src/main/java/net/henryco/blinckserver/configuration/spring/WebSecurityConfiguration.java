package net.henryco.blinckserver.configuration.spring;

import net.henryco.blinckserver.security.auth.FacebookAuthManager;
import net.henryco.blinckserver.security.credentials.AdminCredentials;
import net.henryco.blinckserver.security.credentials.FacebookCredentials;
import net.henryco.blinckserver.security.filter.jwt.JWTAuthFilter;
import net.henryco.blinckserver.security.filter.jwt.JWTLoginFilter;
import net.henryco.blinckserver.security.filter.ResetFilter;
import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.DATA_PATH_POSTFIX;
import static net.henryco.blinckserver.configuration.spring.WebMvcConfiguration.DATA_PATH_URL;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author Henry on 21/08/17.
 */

@SuppressWarnings("UnusedReturnValue")
@PropertySource("classpath:/static/props/base.properties")
@Configuration @ComponentScan(basePackageClasses = {
		FacebookAuthManager.class
}) public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


	private final TokenAuthenticationService userTokenAuthService;
	private final TokenAuthenticationService adminTokenAuthService;

	private final UserDetailsService userDetailsService;
	private final AuthenticationManager facebookAuthManager;

	private @Value("${security.default.admin.name}") String admin_name;
	private @Value("${security.default.admin.password}") String admin_pass;


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
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.and()
				.inMemoryAuthentication()
				.withUser(admin_name)
				.password(admin_pass)
		.roles("MODERATOR", "ADMIN", "USER");
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {

		HttpSecurity httpSecurity = http.csrf().disable();
		httpSecurity = authorizeRequests(httpSecurity);
		httpSecurity = filterUserLoginRequest(httpSecurity);
		httpSecurity = filterAdminLoginRequest(httpSecurity);
		filterJwtHeader(httpSecurity);
	}





	private HttpSecurity authorizeRequests(HttpSecurity http) throws Exception {
		return http.authorizeRequests()
				.antMatchers("/", "/public/**", "/login/**", DATA_PATH_URL+"**").permitAll()
				.antMatchers(GET, "/session/**").permitAll()
				.antMatchers(POST,"/protected/admin/registration").permitAll()
				.anyRequest().authenticated()
		.and();
	}


	private HttpSecurity filterUserLoginRequest(HttpSecurity http) {
		return http.addFilterBefore( // We filter the api/ USER login requests (if user will created if not exists)
				new JWTLoginFilter(
						"/login/user/**",
						facebookAuthManager,
						userTokenAuthService,
						FacebookCredentials.class
				), UsernamePasswordAuthenticationFilter.class
		);
	}


	private HttpSecurity filterAdminLoginRequest(HttpSecurity http) throws Exception {
		return http.addFilterBefore( // We filter the api/ ADMIN login requests
				new JWTLoginFilter(
						"/login/admin/**",
						authenticationManager(),
						adminTokenAuthService,
						AdminCredentials.class
				), UsernamePasswordAuthenticationFilter.class
		);
	}


	private HttpSecurity filterJwtHeader(HttpSecurity http) {

		return http
				.addFilterBefore(
						new ResetFilter(),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore(
						new JWTAuthFilter(userTokenAuthService),
						UsernamePasswordAuthenticationFilter.class
				)

				.addFilterBefore(
						new JWTAuthFilter(
								"/protected/admin/**",
								adminTokenAuthService
						), UsernamePasswordAuthenticationFilter.class
				)
		;
	}


}