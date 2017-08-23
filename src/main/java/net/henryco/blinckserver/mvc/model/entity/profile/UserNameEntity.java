package net.henryco.blinckserver.mvc.model.entity.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Henry on 23/08/17.
 */
@Entity @Data
@NoArgsConstructor
@Table(name = "USER_PROFILE_NAME")
public class UserNameEntity {


	private @Id  @Column(
			name = "name_id",
			unique = true
	) @GeneratedValue(
			strategy = GenerationType.AUTO
	) long id;


	@Column private String username;
	@Column private String firstName;
	@Column private String secondName;
	@Column private String lastName;

}