package net.henryco.blinckserver.mvc.service;

import net.henryco.blinckserver.mvc.model.dao.profile.base.UserBaseProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Henry on 23/08/17.
 */
@Service
public class UserBaseProfileService {

	private final UserBaseProfileDao baseProfileDao;

	@Autowired
	public UserBaseProfileService(UserBaseProfileDao baseProfileDao) {
		this.baseProfileDao = baseProfileDao;
	}




}