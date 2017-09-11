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


	interface DestinationAPI {

		String CONNECTION_ENDPOINT = "/stomp";

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


	/**
	 * 	<h1>EXTERNAL API</h1>
	 *
	 * 	<h2>
	 * 	USE THIS PATHS FOR CONNECTION, SUBSCRIBING AND SENDING <br>
	 * 	DON'T FORGET TO USER PREFIX {/user} before! <br>
	 * 	</h2>
	 */
	interface ExternalAPI {

		String NOTIFICATION = DestinationAPI.Broker.QUEUE + DestinationAPI.Postfix.NOTIFICATION;
		String FRIENDSHIP = DestinationAPI.Broker.MESSAGE + DestinationAPI.Postfix.FRIENDSHIP;
		String SUBGROUP = DestinationAPI.Broker.MESSAGE + DestinationAPI.Postfix.SUBGROUP;
		String GROUP = DestinationAPI.Broker.MESSAGE + DestinationAPI.Postfix.GROUP;

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