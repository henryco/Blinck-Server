package net.henryco.blinckserver.security.filter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Henry on 24/08/17.
 */
public final class ResetFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(null);
		chain.doFilter(request, response);
	}

}