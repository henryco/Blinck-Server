package net.henryco.blinckserver.configuration.project.websocket;

import org.springframework.http.HttpHeaders;

import java.io.Serializable;

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
			String FRIENDSHIP = "/friendship";
			String SUBGROUP = "/subgroup";
			String GROUP = "/group";
		}
	}


	interface Header {

		String USERNAME = "User";
		String ACCESS_TOKEN = HttpHeaders.AUTHORIZATION;
	}


	interface Service {

		String NOTIFICATION = Destination.Broker.QUEUE + Destination.Postfix.NOTIFICATION;
		String FRIENDSHIP = Destination.Broker.MESSAGE + Destination.Postfix.FRIENDSHIP;
		String SUBGROUP = Destination.Broker.MESSAGE + Destination.Postfix.SUBGROUP;
		String GROUP = Destination.Broker.MESSAGE + Destination.Postfix.GROUP;

		static String getFriendship(Serializable id) {
			return FRIENDSHIP + "/" + id;
		}

		static String getSubgroup(Serializable id) {
			return SUBGROUP + "/" + id;
		}

		static String getGroup(Serializable id) {
			return GROUP + "/" + id;
		}
	}

}