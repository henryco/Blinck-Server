package net.henryco.blinckserver.mvc.model.entity.profile.embeded;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @author Henry on 23/08/17.
 */
@Embeddable @Data
@NoArgsConstructor
public class UserNameEntity {


	private @Column(
			nullable = false
	) String firstName;


	private @Column
	String secondName;


	private @Column
	String lastName;


	private @Column
	String nickname;

}