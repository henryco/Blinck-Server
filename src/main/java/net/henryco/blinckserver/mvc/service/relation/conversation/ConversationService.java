package net.henryco.blinckserver.mvc.service.relation.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.ConversationEntity;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Henry on 20/09/17.
 */
public interface ConversationService <T extends ConversationEntity> {


	/**
	 * <h1>Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"topic": 		LONG, 		&nbsp;
	 *		"author": 		LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 */ @Data @NoArgsConstructor @AllArgsConstructor
	final class MessageForm implements Serializable {

		private @JsonProperty("topic") Long topic;
		private @JsonProperty("author") Long author;
		private @JsonProperty("message") String message;
		private @JsonProperty("timestamp") Date date;

		public MessageForm(ConversationEntity conversation) {

			this.topic = conversation.getTopic();
			this.author = conversation.getMessagePart().getAuthor();
			this.date = conversation.getMessagePart().getDate();
			this.message = conversation.getMessagePart().getMessage();
		}

		@JsonIgnore
		public MessagePart getMessagePart(Long userId) {

			MessagePart messagePart = new MessagePart();
			messagePart.setAuthor(userId);
			messagePart.setMessage(getMessage());
			messagePart.setDate(new Date(System.currentTimeMillis()));
			return messagePart;
		}
	}



	T sendMessage(T message);

	T sendMessage(MessageForm message, Long userId);

	long countByTopic(Long topicId);

	void deleteAllInTopic(Long topicId);

	MessageForm[] getLastNByTopic(Long topicId, int page, int size);

}