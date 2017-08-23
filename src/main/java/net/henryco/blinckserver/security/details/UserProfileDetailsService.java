package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 22/08/17.
 */
@Service
public class UserProfileDetailsService implements UserDetailsService {


	private final UserAuthProfileDao authProfileDao;

	@Autowired
	public UserProfileDetailsService(UserAuthProfileDao authProfileDao) {
		this.authProfileDao = authProfileDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserAuthProfile profile = authProfileDao.getById(Long.decode(username));
		if (profile == null) throw new UsernameNotFoundException(username+ " does not exist!");

		return new UserDetailsProfile(profile);
	}

}