package net.henryco.blinckserver.security.details;

import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
public class BlinckDetailsProfileService<KEY extends Serializable> implements UserDetailsService {


	private final BlinckDaoTemplate<? extends BlinckAuthorityEntity<KEY>, KEY> authProfileDao;
	private final Function<String, KEY> keyConverter;
	private final Function<KEY, String> keyDeConverter;


	public BlinckDetailsProfileService(
			final BlinckDaoTemplate<? extends BlinckAuthorityEntity<KEY>, KEY> authProfileDao,
			final Function<String, KEY> keyConverter,
			final Function<KEY, String> keyDeConverter) {

		this.authProfileDao = authProfileDao;
		this.keyConverter = keyConverter;
		this.keyDeConverter = keyDeConverter;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null) throw new UsernameNotFoundException("username is NULL!");

		KEY id = keyConverter.apply(username);
		if (!authProfileDao.isExists(id))
			throw new UsernameNotFoundException(username+ " does not exist!");

		return new BlinckDetailsProfile<>(authProfileDao.getById(id), keyDeConverter);
	}


}