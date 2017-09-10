package net.henryco.blinckserver.configuration.project.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.io.Serializable;
import java.util.Date;

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
			String STAT = "/stat";
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


	/**
	 * <h1>Status JSON</h1>
	 * <h2>
	 *     {&nbsp;
	 *     		"destination":	CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG, &nbsp;
	 *     		"status":		BOOLEAN
	 *     &nbsp;}
	 * </h2>
	 */ @Data
	@NoArgsConstructor
	@AllArgsConstructor
	final class WebSocketStatusJson
			implements Serializable {
		private String destination;
		private Date timestamp;
		private boolean status;
	}

}