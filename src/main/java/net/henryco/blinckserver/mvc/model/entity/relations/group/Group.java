package net.henryco.blinckserver.mvc.model.entity.relations.group;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class Group {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) long id;


}