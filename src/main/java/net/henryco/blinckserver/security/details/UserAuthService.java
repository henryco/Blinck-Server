package net.henryco.blinckserver.security.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 22/08/17.
 */
@Service
public class UserAuthService implements UserDetailsService {


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// TODO: 22/08/17
		System.out.println("UserDetailsService::"+username);
		return new UserDetailsProfile(username);
	}

}