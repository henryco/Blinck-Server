package net.henryco.blinckserver.mvc.service.relation.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.dao.relation.conversation.SubPartyConversationDao;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.SubPartyConversation;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Henry on 18/09/17.
 */
@Service
public class SubPartyConversationService
		extends BlinckDaoProvider<SubPartyConversation, Long> {


	/**
	 * <h1>SubParty Message JSON:</h1>
	 *	<h2>
	 * 	{&nbsp;
	 * 		"sub_party": 	LONG, 		&nbsp;
	 *		"author": 		LONG, 		&nbsp;
	 * 		"message": 		CHAR[512], 	&nbsp;
	 * 		"timestamp": 	DATE/LONG
	 *	&nbsp;}
	 *	</h2>
	 *	@author Henry on 18/09/17.
	 */ @Data @NoArgsConstructor @AllArgsConstructor
	public static final class SubPartyMessageForm
			implements Serializable {

		private @JsonProperty("sub_party") Long subParty;
		private @JsonProperty("author") Long author;
		private @JsonProperty("message") String message;
		private @JsonProperty("timestamp") Date date;

		private SubPartyMessageForm(SubPartyConversation conversation) {

			this.subParty = conversation.getSubParty();
			this.author = conversation.getMessagePart().getAuthor();
			this.date = conversation.getMessagePart().getDate();
			this.message = conversation.getMessagePart().getMessage();
		}
	}


	public SubPartyConversationService(SubPartyConversationDao conversationDao) {
		super(((conversationDao)));
	}

	private SubPartyConversationDao getDao() {
		return provideDao();
	}


	@Transactional
	public SubPartyConversation sendMessage(SubPartyConversation conversation) {
		return getDao().save(conversation);
	}

	@Transactional
	public SubPartyConversation sendMessage(SubPartyMessageForm messageForm, Long userId) {

		MessagePart messagePart = new MessagePart();
		messagePart.setAuthor(userId);
		messagePart.setMessage(messageForm.message);
		messagePart.setDate(new Date(System.currentTimeMillis()));

		SubPartyConversation conversation = new SubPartyConversation();
		conversation.setSubParty(messageForm.subParty);
		conversation.setMessagePart(messagePart);

		return sendMessage(conversation);
	}

	@Transactional
	public SubPartyMessageForm[] getLastNBySubPartyId(Long subPartyId, int page, int size) {

		return getDao().getLastNBySubPartyId(subPartyId, page, size)
				.stream().map(SubPartyMessageForm::new)
		.toArray(SubPartyMessageForm[]::new);
	}

	@Transactional
	public void deleteAllInSubParty(Long subPartyId) {
		getDao().deleteAllBySubPartyId(subPartyId);
	}

	@Transactional
	public Long countBySubPartyId(Long subPartyId) {
		return getDao().countBySubPartyId(subPartyId);
	}

}
