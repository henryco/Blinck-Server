package net.henryco.blinckserver.configuration.project.websocket;

import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

/**
 * @author Henry on 08/09/17.
 */
public class WebSocketAuthAdapter extends ChannelInterceptorAdapter
		implements WebSocketConstants.Header {


	private final TokenAuthenticationService tokenAuthService;


	public WebSocketAuthAdapter(TokenAuthenticationService tokenAuthService) {
		this.tokenAuthService = tokenAuthService;
	}


	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		final StompHeaderAccessor accessor =
				MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (CONNECT == accessor.getCommand()) {

			final String username = checkUsername(accessor.getFirstNativeHeader(USERNAME));
			final String jsonWebToken = checkToken(accessor.getFirstNativeHeader(ACCESS_TOKEN));

			final Authentication authentication = tokenAuthService.getAuthentication(jsonWebToken);
			accessor.setUser(checkAuthentication(authentication, username));
		}

		return message;
	}



	private static
	String checkUsername(String username)
			throws AuthenticationCredentialsNotFoundException {

		if (username == null || username.trim().isEmpty())
			throw new AuthenticationCredentialsNotFoundException("Username is null or empty");
		return username;
	}


	private static
	String checkToken(String jsonWebToken)
			throws AuthenticationCredentialsNotFoundException {

		if (jsonWebToken == null || jsonWebToken.trim().isEmpty())
			throw new AuthenticationCredentialsNotFoundException("Token is null or empty");
		return jsonWebToken;
	}


	private static
	Authentication checkAuthentication(Authentication authentication,
											String username) throws BadCredentialsException {

		if (authentication == null || !authentication.getName().equals(username))
			throw new BadCredentialsException("Bad credentials for user " + username);
		return authentication;
	}

}