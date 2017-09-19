package net.henryco.blinckserver.configuration.project.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.configuration.project.websocket.WebSocketConstants;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.UpdateNotification;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Henry on 07/09/17.
 */
public interface BlinckNotification {

	String WEB_SOCKET_NOTIFICATION_ENDPOINT = WebSocketConstants.ExternalAPI.NOTIFICATION;

	interface TYPE {

		String FRIEND_MESSAGE_STOMP = "friendship_message_stomp";
		String FRIEND_MESSAGE_REST = "friendship_message_rest";
		String FRIEND_REQUEST = "friendship_request";
		String FRIEND_ACCEPTED = "friendship_accepted";
		String FRIEND_DECLINED = "friendship_declined";
		String FRIEND_DELETED = "friendship_deleted";

		String CUSTOM_SUB_PARTY_REMOVE = "custom_sub_party_removed";
		String CUSTOM_SUB_PARTY_JOIN = "custom_sub_party_joined";
		String CUSTOM_SUB_PARTY_INVITE = "custom_sub_party_invite";
		String CUSTOM_SUB_PARTY_LEAVE = "custom_sub_party_leave";

		String SUB_PARTY_MESSAGE_STOMP = "sub_party_message_stomp";
		String SUB_PARTY_MESSAGE_REST = "sub_party_message_rest";
		String SUB_PARTY_FOUND = "sub_party_found";
		String SUB_PARTY_IN_QUEUE = "sub_party_in_queue";

		String PARTY_MESSAGE_STOMP = "party_message_stomp";
		String PARTY_MESSAGE_REST = "party_message_rest";
		String PARTY_FOUND = "party_found";
		String PARTY_MEETING_SET = "party_meeting_set";
		String PARTY_MEETING_PROPOSITION = "party_meeting_proposition";
		String PARTY_MEETING_VOTE = "party_meeting_vote";
		String PARTY_MEETING_VOTE_FINAL = "party_meeting_vote_final";
		String PARTY_MEETING_VOTE_FINAL_FAIL = "party_meeting_vote_final_fail";
		String PARTY_MEETING_VOTE_FINAL_SUCCESS = "party_meeting_vote_final_success";

		String QUEUE_LEAVE = "queue_leave";
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
	final class JsonNotificationForm
			implements Serializable {

		private Long id;
		private String type;
		private String info;
		private Date timestamp;

		public JsonNotificationForm(UpdateNotification notification) {
			this.id = notification.getId();
			this.type = notification.getDetails().getType();
			this.info = notification.getDetails().getNotification();
			this.timestamp = notification.getDate();
		}

	}


	/**
	 * <h1>Simple notification JSON</h1><br>
	 *     <h2>
	 *         {&nbsp;
	 *             "receiver_id":	LONG, &nbsp;
	 *             "type":			CHAR[255], &nbsp;
	 *             "notification":	CHAR[255]
	 *         &nbsp;}
	 *     </h2>
	 */ @Data @NoArgsConstructor @AllArgsConstructor
	final class SimpleNotification
			implements Serializable{

		private @JsonProperty("receiver_id") Long targetUserId;
		private @JsonProperty String type;
		private @JsonProperty String notification;
	}
}