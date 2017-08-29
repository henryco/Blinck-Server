package net.henryco.blinckserver.mvc.model.dao.infrastructure.imp;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.BlackListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.BlackList;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class BlackListDaoImp implements BlackListDao {

	@Override
	public BlackList getById(Long id) {
		return null;
	}

	@Override
	public BlackList save(BlackList blackList) {
		return null;
	}

	@Override
	public boolean isExists(Long id) {
		return false;
	}

	@Override
	public void deleteById(Long id) {

	}
}