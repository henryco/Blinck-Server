package net.henryco.blinckserver.configuration.websocket;

import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author Henry on 08/09/17.
 */
@Configuration
@EnableWebSocketMessageBroker
@Order(HIGHEST_PRECEDENCE + 99)
public class WebSocketBrokerConfiguration
		extends AbstractWebSocketMessageBrokerConfigurer {


	private final TokenAuthenticationService authenticationService;


	@Autowired
	public WebSocketBrokerConfiguration(@Qualifier("UserTokenAuthService")
												 TokenAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}


	@Override
	public void configureClientInboundChannel(final ChannelRegistration registration) {
		registration.setInterceptors(new WebSocketAuthAdapter(authenticationService));
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp/chat").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue", "/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

}