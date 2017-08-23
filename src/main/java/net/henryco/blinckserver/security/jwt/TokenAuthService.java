package net.henryco.blinckserver.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;


/**
 * @author Henry on 22/08/17.
 */

@Service
@PropertySource("classpath:/static/props/base.properties")
@SuppressWarnings("WeakerAccess")
public final class TokenAuthService {

	private final String app_secret;
	private final String default_role;
	private final String header_string;
	private final String token_prefix;

	private final long expiration_time;


	@Autowired
	public TokenAuthService(Environment environment) {
		app_secret = environment.getProperty("security.jwt.secret");
		default_role = environment.getProperty("security.roles.default", "ROLE_USER");
		header_string = environment.getProperty("security.jwt.header", "Authorization");
		token_prefix = environment.getProperty("security.jwt.prefix", "Bearer");
		expiration_time = Long.decode(environment.getProperty("security.jwt.expiration", "864000000"));
	}


	public void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration_time))
				.signWith(SignatureAlgorithm.HS512, app_secret)
				.compact();
		res.addHeader(header_string, token_prefix + " " + JWT);
	}



	public Authentication getAuthentication(HttpServletRequest request) {

		final String token = request.getHeader(header_string);
		if (token == null) return null;

		final String user;
		try {
			user = Jwts.parser()
					.setSigningKey(app_secret)
					.parseClaimsJws(token.replace(token_prefix, "")).getBody()
			.getSubject();
		} catch (ExpiredJwtException e) { return null; }

		return user == null ? null
		: new UsernamePasswordAuthenticationToken(user, null,
				Collections.singletonList(new SimpleGrantedAuthority(default_role))
		);
	}


}