package net.henryco.blinckserver.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;


/**
 * @author Henry on 22/08/17.
 */
@SuppressWarnings("WeakerAccess")
public class TokenAuthService {

	private static final long EXPIRATION_TIME = 864_000_000; // 10 days
	private static final String SECRET = "jnfv78rg34badfhvq784";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";


	@SuppressWarnings("WeakerAccess")
	public static void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}


	@SuppressWarnings("WeakerAccess")
	public static Authentication getAuthentication(HttpServletRequest request) {

		String token = request.getHeader(HEADER_STRING);
		if (token == null) return null;

		String user = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody()
		.getSubject();

		return user == null ? null
				: new UsernamePasswordAuthenticationToken(user, null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}


}