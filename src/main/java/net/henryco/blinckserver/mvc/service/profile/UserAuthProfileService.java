package net.henryco.blinckserver.mvc.service.profile;

import net.henryco.blinckserver.mvc.model.dao.security.UserAuthProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserAuthProfileService {

	private final UserAuthProfileDao authProfileDao;

	@Autowired
	public UserAuthProfileService(UserAuthProfileDao authProfileDao) {
		this.authProfileDao = authProfileDao;
	}



}