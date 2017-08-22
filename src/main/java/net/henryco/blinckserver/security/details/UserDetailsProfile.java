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
	public UserDetailsProfile(String username) {
		this.username = username;
		System.out.println("UserDetailsProfile");
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(/* TODO string array*/);
	}


	@Override
	public String getPassword() {
		System.out.println("UserDetailsProfile::PASSWORD");
		return "password1";
	}

	@Override
	public String getUsername() {
		System.out.println("UserDetailsProfile::NAME");
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
