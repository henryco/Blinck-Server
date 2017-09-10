package net.henryco.blinckserver.configuration.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;

/**
 * @author Henry on 09/09/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {ChannelSecurityInterceptor.class}) // <-- DON'T FUCKIN TOUCH THIS, SRSLY, NEVER
public class WebSocketAuthConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
		messages.anyMessage().authenticated();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true; // Disable CSRF for endpoints.
	}

}