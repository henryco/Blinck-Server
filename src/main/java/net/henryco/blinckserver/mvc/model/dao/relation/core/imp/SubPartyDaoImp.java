package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Random;

/**
 * @author Henry on 29/08/17.
 */
@Repository
@Transactional
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
	public List<SubParty> getAllWithUserInParty(Long user) {
		return getRepository().getAllByUsersIsContainingAndPartyNotNullAndParty_Details_InQueueIsFalse(user);
	}

	@Override
	public List<SubParty> getAllWithUserInQueue(Long user) {
		return getRepository().getAllByUsersIsContainingAndDetails_InQueueIsTrue(user);
	}

	@Override
	public List<SubParty> getAllWithUser(Long user) {
		return getRepository().getAllByUsersIsContaining(user);
	}

	@Override
	public List<SubParty> getAllInQueue(String typeWanted, String typeIdent, Integer dimension, int page, int size) {
		return getRepository().getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
				typeWanted, typeIdent, dimension, true, new PageRequest(page, size)
		);
	}

	@Override
	public boolean existsWithUser(Long id, Long userId) {
		return getRepository().existsByIdAndUsersIsContaining(id, userId);
	}

	@Override @Transactional
	public SubParty getRandomFirstInQueue(String typeWanted, String typeIdent, Integer dimension) {
		try {

			List<SubParty> last =
					getRepository().getFirst100ByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
							typeWanted, typeIdent, dimension, true
			);
			return last.isEmpty() ? null : last.get(new Random().nextInt(last.size()));
		} catch (EntityNotFoundException e) {
			return null;
		}
	}
}