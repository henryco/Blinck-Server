package net.henryco.blinckserver.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Henry on 23/08/17.
 */
@Component
public class FacebookAuthManager implements AuthenticationManager {





	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {


		Object principal = authentication.getPrincipal();
		Object credentials = authentication.getCredentials();

		System.out.println(principal.getClass() + ": " +principal);
		System.out.println(credentials.getClass() + ": " +credentials);


		Facebook facebook = new FacebookTemplate(credentials.toString());
		if (!facebook.isAuthorized())
			throw new SessionAuthenticationException("FACEBOOK UNAUTHORIZED");

		User userProfile = facebook.userOperations().getUserProfile();
		String id = userProfile.getId();

		if (credentials.toString().equals(id)) {

		}

		return null;
	}
}
