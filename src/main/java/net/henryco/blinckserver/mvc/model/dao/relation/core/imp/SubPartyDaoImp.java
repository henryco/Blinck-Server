package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.SubParty;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class SubPartyDaoImp implements SubPartyDao {
	@Override
	public SubParty getById(Long id) {
		return null;
	}

	@Override
	public SubParty save(SubParty subParty) {
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