package net.henryco.blinckserver.security.details.user;

import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Henry on 22/08/17.
 */
public class UserDetailsProfile implements UserDetails {


 	private final UserAuthProfile authProfile;


	public UserDetailsProfile(UserAuthProfile authProfile) {
		this.authProfile = authProfile;
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
		return Long.toString(authProfile.getId());
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