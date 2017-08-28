package net.henryco.blinckserver.mvc.model.entity.relations;


import java.util.Date;

/**
 * @author Henry on 28/08/17.
 */
public interface ConversationEntity<ID, AUTHOR> {

	ID getId();

	String getMessage();

	Date getDate();

	AUTHOR getAuthor();

}