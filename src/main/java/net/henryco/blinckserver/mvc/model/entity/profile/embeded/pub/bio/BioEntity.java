package net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 21/09/17.
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BioEntity {

	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";


	private @Embedded @JoinColumn(
			name = "name_id"
	) UserNameEntity userName;


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

}