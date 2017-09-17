package net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;

import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 17/09/17.
 */
@Embeddable
@Data @NoArgsConstructor
@AllArgsConstructor
public class MessagePart {

	private @Column(
			name = "message",
			length = 512
	) String message;


	private @Column(
			name = "time_stamp",
			updatable = false,
			nullable = false
	) @Temporal(
			TIMESTAMP
	) Date date;


	private @Column(
			name = "author",
			nullable = false,
			updatable = false
	) Long author;

}