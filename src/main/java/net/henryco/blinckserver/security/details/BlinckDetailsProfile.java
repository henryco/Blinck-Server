package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

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


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(authProfile.getAuthorityArray());
	}

	@Override
	public String getPassword() {
		return authProfile.getPassword();
	}

	@Override
	public String getUsername() {
		return keyConverter.apply(authProfile.getId());
	}

	@Override
	public boolean isAccountNonExpired() {
		return !authProfile.isExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !authProfile.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !authProfile.isExpired();
	}

	@Override
	public boolean isEnabled() {
		return authProfile.isEnabled();
	}

}