package net.henryco.blinckserver.security.jwt.filter;

import net.henryco.blinckserver.security.jwt.service.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Henry on 22/08/17.
 */

public class JWTAuthFilter extends GenericFilterBean {


	private final TokenAuthenticationService userTokenAuthService;

	public JWTAuthFilter(TokenAuthenticationService userTokenAuthService) {
		this.userTokenAuthService = userTokenAuthService;
	}


	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain filterChain) throws IOException, ServletException {

		Authentication authentication = userTokenAuthService.getAuthentication((HttpServletRequest)request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

}