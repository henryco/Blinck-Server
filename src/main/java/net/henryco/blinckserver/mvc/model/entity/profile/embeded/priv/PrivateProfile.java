package net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Henry on 14/09/17.
 */
@Embeddable @Data
@NoArgsConstructor
public class PrivateProfile {

	private @Column(
			name = "email"
	) String email;

}