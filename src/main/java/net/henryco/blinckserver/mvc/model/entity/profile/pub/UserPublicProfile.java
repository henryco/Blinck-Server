package net.henryco.blinckserver.mvc.model.entity.profile.pub;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 30/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class UserPublicProfile {


	private @Id @Column(
			name = "id",
			unique = true
	) long id;


	private @Column(
			name = "gender"
	) String gender;


	private @Column(
			name = "about",
			length = 510
	) String about;


	private @Column(
			name = "birthday"
	) @Temporal(
			TIMESTAMP
	) Date birthday;


	private @OneToOne(
			cascade = CascadeType.ALL,
			optional = false
	) @JoinColumn(
			name = "name_id",
			unique = true
	) UserNameEntity userName;


}