package net.henryco.blinckserver.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import net.henryco.blinckserver.security.jwt.credentials.JWTLoginCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Henry on 22/08/17.
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {


	private final TokenAuthenticationService userTokenAuthService;
	private final Class<? extends JWTLoginCredentials> loginCredentialsClass;


	public JWTLoginFilter(String url,
						  AuthenticationManager authManager,
						  TokenAuthenticationService userTokenAuthService,
						  Class<? extends JWTLoginCredentials> loginCredentialsClass) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		this.userTokenAuthService = userTokenAuthService;
		this.loginCredentialsClass = loginCredentialsClass;
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		JWTLoginCredentials credentials = new ObjectMapper()
				.readValue(req.getInputStream(), loginCredentialsClass);

		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getPrincipal(),
						credentials.getCredentials(),
						credentials.getAuthorities()
				)
		);
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth)
			throws IOException, ServletException {

		userTokenAuthService.addAuthentication(res, auth.getName());
	}
}