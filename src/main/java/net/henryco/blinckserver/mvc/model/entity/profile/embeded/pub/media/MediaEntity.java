package net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;

/**
 * @author Henry on 21/09/17.
 */
@Embeddable @Data
@NoArgsConstructor
public class MediaEntity {


	private @Embedded @JoinColumn(
			nullable = false
	) UserPhotoEntity photo;

}