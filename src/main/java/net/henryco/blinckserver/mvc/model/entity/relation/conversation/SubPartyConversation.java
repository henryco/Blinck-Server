package net.henryco.blinckserver.mvc.model.entity.relation.conversation;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class SubPartyConversation
		implements ConversationEntity {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Embedded @JoinColumn(
			updatable = false,
			nullable = false,
			name = "message"
	) MessagePart messagePart;


	private @Column(
			name = "sub_party",
			nullable = false,
			updatable = false
	) Long subParty;


	@Override
	public Long getTopic() {
		return getSubParty();
	}
}