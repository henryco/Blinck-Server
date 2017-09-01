package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
public class BlinckDetailsProfile<KEY extends Serializable> implements UserDetails {

	private final BlinckAuthorityEntity<KEY> authProfile;
	private final Function<KEY, String> keyConverter;

	public BlinckDetailsProfile(BlinckAuthorityEntity<KEY> authProfile,
								Function<KEY, String> keyDeConverter) {
		this.authProfile = authProfile;
		this.keyConverter = keyDeConverter;
	}


	@Override @Transactional
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(authProfile.getAuthorityArray());
	}

	@Override @Transactional
	public String getPassword() {
		return authProfile.getPassword();
	}

	@Override @Transactional
	public String getUsername() {
		return keyConverter.apply(authProfile.getId());
	}

	@Override @Transactional
	public boolean isAccountNonExpired() {
		return !authProfile.isExpired();
	}

	@Override @Transactional
	public boolean isAccountNonLocked() {
		return !authProfile.isLocked();
	}

	@Override @Transactional
	public boolean isCredentialsNonExpired() {
		return !authProfile.isExpired();
	}

	@Override @Transactional
	public boolean isEnabled() {
		return authProfile.isEnabled();
	}

}