package net.henryco.blinckserver.util.entity;

import net.henryco.blinckserver.util.Utils;

/**
 * @author Henry on 23/08/17.
 */
public interface BlinckAuthorityEntity {


	String getAuthorities();
	@Deprecated void setAuthorities(String authorities);


	default String[] getAuthorityArray() {
		return Utils.stringToArray(getAuthorities());
	}

	default void setAuthorityArray(String ... authorities) {
		setAuthorities(Utils.arrayToString(authorities));
	}

}