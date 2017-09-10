package net.henryco.blinckserver.security.token.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.security.token.processor.TokenAuthenticationProcessor;
import net.henryco.blinckserver.util.test.BlinckTestName;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Henry on 23/08/17.
 */
@SuppressWarnings("WeakerAccess")
public abstract class TokenAuthenticationService {


	protected abstract TokenAuthenticationProcessor getProcessor();

	protected abstract String getTokenSecret();

	protected String getDefaultRole() {
		return "ROLE_USER";
	}

	protected String getTokenHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

	protected String getTokenPrefix() {
		return "Bearer";
	}

	protected Long getExpirationTime() {
		return 864_000_000L;
	}


	@Data @AllArgsConstructor @NoArgsConstructor
	private static final class TokenPayload {
		private String username;
		private String[] authorities;
	}




	public final void addAuthentication(HttpServletResponse res, Authentication auth) {

		Stream<String> stream = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority);

		String JWT = createAuthenticationToken(auth.getName(), stream.toArray(String[]::new));
		res.addHeader(getTokenHeader(), getTokenPrefix() + " " + JWT);

		if (getProcessor() != null) getProcessor().addAuthentication(auth);
	}



	public final Authentication getAuthentication(HttpServletRequest request) {
		return getAuthentication(request.getHeader(getTokenHeader()));
	}


	@SuppressWarnings("ConstantConditions")
	public final Authentication getAuthentication(String jsonWebToken) {

		try {
			TokenPayload payload = readAuthenticationToken(jsonWebToken);

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					payload.username,
					null,
					grantAuthorities(payload.authorities)
			);

			if (getProcessor() == null) return auth;
			return getProcessor().processAuthentication(auth);

		} catch (JwtException | NullPointerException e) {
			return null;
		}
	}



	@BlinckTestName("createAuthenticationToken")
	private String createAuthenticationToken(String username, String ... authorities) {

		try {
			final TokenPayload payload = new TokenPayload(username, authorities);
			return parseTo(new ObjectMapper().writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return parseTo(username);
		}
	}


	@BlinckTestName("readAuthenticationToken")
	private TokenPayload readAuthenticationToken(String token) {
		try {
			return new ObjectMapper().readValue(parseFrom(token), TokenPayload.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	@BlinckTestName("parseTo")
	private String parseTo(String payload) {
		return Jwts.builder()
				.setSubject(payload)
				.setExpiration(new Date(System.currentTimeMillis() + getExpirationTime()))
				.signWith(SignatureAlgorithm.HS512, getTokenSecret())
		.compact();
	}


	@BlinckTestName("parseFrom")
	private String parseFrom(String token) {
		if (token == null) return null;

		return Jwts.parser()
				.setSigningKey(getTokenSecret())
				.parseClaimsJws(token.replace(getTokenPrefix(), "")).getBody()
		.getSubject();
	}


	@BlinckTestName("grantAuthorities")
	private Collection<? extends GrantedAuthority> grantAuthorities(String ... authorities) {

		try {
			return Arrays.stream(authorities)
					.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
		} catch (Exception ignored) { }
		return defaultAuthorities();
	}


	@BlinckTestName("grantDefaultAuthorities")
	private Collection<? extends GrantedAuthority> defaultAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(getDefaultRole()));
	}


}