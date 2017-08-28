package net.henryco.blinckserver.mvc.model.entity.relations.dual;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class DualRelation {


	private @Id @Column(
			name = "id",
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) long id;


	private @Column(
			name = "relation_type"
	) int relationType;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "user_id_1"
	) UserBaseProfile userId1;


	private @ManyToOne(
			cascade = ALL,
			optional = false
	) @JoinColumn(
			name = "user_id_2"
	) UserBaseProfile userId2;


}