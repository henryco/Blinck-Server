package net.henryco.blinckserver.configuration.project.websocket;

import org.springframework.http.HttpHeaders;

/**
 * @author Henry on 10/09/17.
 */
public interface WebSocketConstants {


	interface Destination {

		String ENDPOINT = "/stomp";

		interface Application {
			String APP = "/app";
		}

		interface Broker {
			String QUEUE = "/queue";
			String TOPIC = "/topic";
			String MESSAGE = "/message";
		}

		interface Postfix {
			String NOTIFICATION = "/notification";
		}


	}


	interface Header {

		String USERNAME = "User";
		String ACCESS_TOKEN = HttpHeaders.AUTHORIZATION;
	}


	interface Service {
		String NOTIFICATION = Destination.Broker.QUEUE + Destination.Postfix.NOTIFICATION;
	}

}
