package net.henryco.blinckserver.util.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.henryco.blinckserver.util.Utils;

import java.io.Serializable;

/**
 * @author Henry on 23/08/17.
 */
public interface BlinckAuthorityEntity<KEY extends Serializable> {


	KEY getId();

	boolean isLocked();
	boolean isExpired();
	boolean isEnabled();

	String getPassword();
	String getAuthorities();



	@Deprecated void setAuthorities(String authorities);


	default @JsonIgnore
	String[] getAuthorityArray() {
		return Utils.stringToArray(getAuthorities());
	}

	default
	void setAuthorityArray(String ... authorities) {
		setAuthorities(Utils.arrayToString(authorities));
	}

}