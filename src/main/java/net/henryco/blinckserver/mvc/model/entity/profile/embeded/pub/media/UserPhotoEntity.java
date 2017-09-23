package net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.Utils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Henry on 21/09/17.
 */
@Embeddable
@Data @NoArgsConstructor
public class UserPhotoEntity {

	private @Column
	String avatar;


	/**
	 * <h2>Array of image links stored as string.</h2>
	 * <h3>
	 *     Should be coded and decoded via
	 *     {@link Utils#arrayToString(String[])} and
	 *     {@link Utils#stringToArray(String)}.<br>
	 *
	 *     Instead getter and setter use
	 *     {@link #getPhotoArray()} and
	 *     {@link #setPhotoArray(String...)}.
	 * </h3>
	 *
	 */ private @Column(
	 		length = 510
	) String photos;


	@JsonIgnore
	public String[] getPhotoArray() {
		return Utils.stringToArray(photos);
	}

	@JsonIgnore
	public void setPhotoArray(String ... photos) {
		this.photos = Utils.arrayToString(photos);
	}

}