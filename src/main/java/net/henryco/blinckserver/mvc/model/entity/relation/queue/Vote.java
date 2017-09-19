package net.henryco.blinckserver.mvc.model.entity.relation.queue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 19/09/17.
 */
@Entity @Data
@NoArgsConstructor
public class Vote {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "topic",
			nullable = false,
			updatable = false
	) String topic;


	private @Column(
			name = "voter",
			nullable = false,
			updatable = false
	) String voter;


	private @Column(
			name = "voice",
			nullable = false
	) Boolean voice;


	public Vote(Serializable topic, Serializable voter, boolean voice) {
		this.topic = topic.toString();
		this.voter = voter.toString();
		this.voice = voice;
	}
}