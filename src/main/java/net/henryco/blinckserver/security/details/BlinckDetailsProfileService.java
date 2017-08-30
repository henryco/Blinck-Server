package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
public class BlinckDetailsProfileService<KEY> implements UserDetailsService {


	private final BlinckDaoTemplate<? extends BlinckAuthorityEntity, KEY> authProfileDao;
	private final Function<String, KEY> keyConverter;


	public BlinckDetailsProfileService(
			final BlinckDaoTemplate<? extends BlinckAuthorityEntity, KEY> authProfileDao,
			final Function<String, KEY> keyConverter) {
		this.authProfileDao = authProfileDao;
		this.keyConverter = keyConverter;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null) throw new UsernameNotFoundException("username is NULL!");
		KEY id = keyConverter.apply(username);
		if (!authProfileDao.isExists(id))
			throw new UsernameNotFoundException(username+ " does not exist!");
		return new BlinckDetailsProfile(authProfileDao.getById(id));
	}


}