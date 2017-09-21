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


	private @Embedded @JoinColumn(
			name = "bio"
	) BioEntity bio;




}