package net.henryco.blinckserver.configuration.project.notification;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Henry on 07/09/17.
 */
public interface BlinckNotification {


	interface TYPE {

		String FRIEND_MESSAGE = "friendship_message";
		String FRIEND_REQUEST = "friendship_request";
		String FRIEND_ACCEPTED = "friendship_accepted";
		String FRIEND_DECLINED = "friendship_declined";
		String FRIEND_DELETED = "friendship_deleted";
	}


	/**
	 * <h1>NotificationForm JSON</h1>
	 * <h2>
	 *     {&nbsp;
	 *     		"id":			LONG, &nbsp;
	 *     		"type":			CHAR[255], &nbsp;
	 *     		"info":			CHAR[255], &nbsp;
	 *     		"timestamp":	DATE/LONG
	 *     &nbsp;}
	 * </h2>
	 */ @Data
	@NoArgsConstructor
	final class JsonForm
			implements Serializable {

		private Long id;
		private String type;
		private String info;
		private Date timestamp;

		public JsonForm(UpdateNotification notification) {
			this.id = notification.getId();
			this.type = notification.getDetails().getType();
			this.info = notification.getDetails().getNotification();
			this.timestamp = notification.getDate();
		}

	}


}