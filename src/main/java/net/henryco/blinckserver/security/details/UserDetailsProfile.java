package net.henryco.blinckserver.security.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Henry on 22/08/17.
 */
public class UserDetailsProfile implements UserDetails {


 	private final String username;
 	private final UserAuthProfile authProfile;


	public UserDetailsProfile(String username,
							  UserAuthProfile authProfile) {
		this.username = username;
		this.authProfile = authProfile;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(authProfile.getAuthoritiesArray());
	}


	@Override
	public String getPassword() {
		return authProfile.getPassword();
	}

	@Override
	public String getUsername() {
		return username;
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
