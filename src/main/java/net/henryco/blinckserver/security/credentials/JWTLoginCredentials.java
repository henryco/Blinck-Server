package net.henryco.blinckserver.security.credentials;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Henry on 24/08/17.
 */
@SuppressWarnings("WeakerAccess")
public abstract class JWTLoginCredentials implements Serializable {


	public Object getPrincipal() {
		return null;
	}

	public Object getCredentials() {
		return null;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

}