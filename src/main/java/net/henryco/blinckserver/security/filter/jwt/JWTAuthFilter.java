package net.henryco.blinckserver.security.filter.jwt;

import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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

public final class JWTAuthFilter extends GenericFilterBean {


	private final TokenAuthenticationService tokenAuthService;
	private final RequestMatcher requiresAuthenticationRequestMatcher;


	public JWTAuthFilter(TokenAuthenticationService tokenAuthService) {
		this.tokenAuthService = tokenAuthService;
		this.requiresAuthenticationRequestMatcher = null;
	}


	public JWTAuthFilter(String url, TokenAuthenticationService tokenAuthService) {
		this.tokenAuthService = tokenAuthService;
		this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(url);
	}




	@Override
	public final void doFilter(ServletRequest request,
							   ServletResponse response,
							   FilterChain filterChain) throws IOException, ServletException {

		checkYourPrivileges((HttpServletRequest) request);
		filterChain.doFilter(request, response);
	}




	private void checkYourPrivileges(HttpServletRequest request){
		if (isRequestMatches(request) || isAuthNull()) {
			Authentication authentication = tokenAuthService.getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}



	private boolean isRequestMatches(HttpServletRequest request) {
		return requiresAuthenticationRequestMatcher != null &&
				requiresAuthenticationRequestMatcher.matches(request);
	}



	private boolean isAuthNull() {
		return requiresAuthenticationRequestMatcher == null &&
				SecurityContextHolder.getContext().getAuthentication() == null;
	}

}