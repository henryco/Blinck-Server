package net.henryco.blinckserver.mvc.model.dao.security.imp;

import net.henryco.blinckserver.mvc.model.dao.security.SessionWhiteListDao;
import net.henryco.blinckserver.mvc.model.entity.security.SessionWhiteList;
import net.henryco.blinckserver.mvc.model.repository.security.SessionWhiteListRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 01/09/17.
 */
@Repository
public class SessionWhiteListDaoImp
		extends BlinckRepositoryProvider<SessionWhiteList, Long>
		implements SessionWhiteListDao {

	@Autowired
	public SessionWhiteListDaoImp(SessionWhiteListRepository repository) {
		super(repository);
	}


	private SessionWhiteListRepository getRepository() {
		return provideRepository();
	}


	@Override
	public void removeAdminSession(String id) {
		getRepository().deleteAllByAdminId(id);
	}

	@Override
	public void removeUserSession(Long id) {
		getRepository().deleteAllByUserId(id);
	}

	@Override
	public boolean isAdminSessionExists(String id) {
		return getRepository().existsByAdminId(id);
	}

	@Override
	public boolean isUserSessionExists(Long id) {
		return getRepository().existsByUserId(id);
	}

}