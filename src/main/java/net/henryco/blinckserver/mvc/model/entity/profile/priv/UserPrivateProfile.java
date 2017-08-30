package net.henryco.blinckserver.mvc.model.entity.profile.priv;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Henry on 30/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class UserPrivateProfile {


	private @Id @Column(
			name = "id",
			unique = true
	) long id;


	private @Column(
			name = "email"
	) String email;


}