package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class SubPartyDaoImp
		extends BlinckRepositoryProvider<SubParty, Long>
		implements SubPartyDao {

	public SubPartyDaoImp(SubPartyRepository repository) {
		super(repository);
	}

	private SubPartyRepository getRepository() {
		return provideRepository();
	}

	@Override
	public List<SubParty> getAllWithUser(Long user) {
		return getRepository().getAllByUsersIsContaining(user);
	}

	@Override
	public List<SubParty> getAllWithUser(Long user, int page, int size) {
		return getRepository().getAllByUsersIsContaining(user, new PageRequest(page, size));
	}

	@Override
	public List<SubParty> getAllInQueue(String typeWanted, String typeIdent, Integer dimension) {
		return getRepository().getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
				typeWanted, typeIdent, dimension, true
		);
	}

	@Override
	public List<SubParty> getAllInQueue(String typeWanted, String typeIdent, Integer dimension, int page, int size) {
		return getRepository().getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
				typeWanted, typeIdent, dimension, true, new PageRequest(page, size)
		);
	}

	@Override
	public SubParty getFirstInQueue(String typeWanted, String typeIdent, Integer dimension) {
		try {
			return getRepository().getTopByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
					typeWanted, typeIdent, dimension, true
			);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

}