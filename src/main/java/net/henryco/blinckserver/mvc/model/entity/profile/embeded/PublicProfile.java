package net.henryco.blinckserver.mvc.model.entity.profile.embeded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 30/08/17.
 */
@Embeddable @Data
@NoArgsConstructor
public class PublicProfile {

	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";


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


	private @Embedded @JoinColumn(
			name = "name_id"
	) UserNameEntity userName;


}