package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.Party;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class PartyDaoImp implements PartyDao{
	@Override
	public Party getById(Long id) {
		return null;
	}

	@Override
	public Party save(Party party) {
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