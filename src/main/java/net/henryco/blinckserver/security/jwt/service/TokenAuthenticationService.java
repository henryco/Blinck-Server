package net.henryco.blinckserver.security.jwt.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * @author Henry on 23/08/17.
 */
@SuppressWarnings("WeakerAccess")
public abstract class TokenAuthenticationService {


	protected abstract String getTokenSecret();

	protected abstract UserDetailsService getUserDetailsService();

	protected String getDefaultRole() {
		return "ROLE_USER";
	}

	protected String getTokenHeader() {
		return "Authorization";
	}

	protected String getTokenPrefix() {
		return "Bearer";
	}

	protected Long getExpirationTime() {
		return 864_000_000L;
	}



	@BlinckTestName("createAuthenticationToken")
	private String createAuthenticationToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + getExpirationTime()))
				.signWith(SignatureAlgorithm.HS512, getTokenSecret())
		.compact();
	}


	@BlinckTestName("readAuthenticationToken")
	private String readAuthenticationToken(String token) {

		if (token == null) return null;

		return Jwts.parser()
				.setSigningKey(getTokenSecret())
				.parseClaimsJws(token.replace(getTokenPrefix(), "")).getBody()
		.getSubject();
	}


	@BlinckTestName("grantDefaultAuthorities")
	private Collection<? extends GrantedAuthority> defaultAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(getDefaultRole()));
	}


	@BlinckTestName("grantAuthorities")
	private Collection<? extends GrantedAuthority> grantAuthorities(String username) {

		try {
			return getUserDetailsService().loadUserByUsername(username).getAuthorities();
		} catch (Exception ignored) { }
		return defaultAuthorities();
	}


	public final void addAuthentication(HttpServletResponse res, String username) {
		String JWT = createAuthenticationToken(username);
		res.addHeader(getTokenHeader(), getTokenPrefix() + " " + JWT);
	}


	public final Authentication getAuthentication(HttpServletRequest request) {

		final String token = request.getHeader(getTokenHeader());
		if (token == null) return null;

		final String user;
		try {
			user = readAuthenticationToken(token);
		} catch (JwtException e) { return null; }

		return user == null ? null :
				new UsernamePasswordAuthenticationToken(
						user, null, grantAuthorities(user)
		);
	}

}