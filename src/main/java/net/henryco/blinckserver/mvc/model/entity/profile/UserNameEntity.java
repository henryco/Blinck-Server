package net.henryco.blinckserver.mvc.model.entity.profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @author Henry on 23/08/17.
 */
@Entity @Data
@NoArgsConstructor
@Proxy(lazy = false)
@Table(name = "USER_PROFILE_NAME")
public class UserNameEntity {


	private @Id @Column(
			name = "name_id",
			unique = true
	) long id;


	private @Column(
			nullable = false
	) String firstName;


	@Column private String secondName;
	@Column private String lastName;

}