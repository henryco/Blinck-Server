package net.henryco.blinckserver.mvc.model.dao.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
public interface SubPartyDao extends BlinckDaoTemplate<SubParty, Long> {

	List<SubParty> getAllWithUser(Long user);
	List<SubParty> getAllWithUser(Long user, int page, int size);

	List<SubParty> getAllInQueue(String typeWanted, String typeIdent, Integer dimension);
	List<SubParty> getAllInQueue(String typeWanted, String typeIdent, Integer dimension, int page, int size);

	SubParty getFirstInQueue(String typeWanted, String typeIdent, Integer dimension);
}