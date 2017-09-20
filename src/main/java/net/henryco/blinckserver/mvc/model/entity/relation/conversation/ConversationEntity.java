package net.henryco.blinckserver.mvc.model.entity.relation.conversation;

import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;

/**
 * @author Henry on 20/09/17.
 */
public interface ConversationEntity {

	Long getId();
	Long getTopic();
	MessagePart getMessagePart();
}
