package net.henryco.blinckserver.mvc.model.entity.profile.embeded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Henry on 21/09/17.
 */
@Embeddable @Data
@NoArgsConstructor
public class MediaEntity {

	private @Column
	String avatar;

}