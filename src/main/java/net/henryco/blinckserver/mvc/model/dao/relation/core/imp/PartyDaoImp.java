package net.henryco.blinckserver.mvc.model.dao.relation.core.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.repository.relation.core.PartyRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class PartyDaoImp
		extends BlinckRepositoryProvider<Party, Long>
		implements PartyDao {

	public PartyDaoImp(PartyRepository repository) {
		super(repository);
	}


	private PartyRepository getRepository() {
		return provideRepository();
	}


	@Override @Transactional
	public Party getRandomFirstInQueue(String typeWanted, String typeIdent, Integer dimension) {

		try {
			List<Party> all = getRepository()
					.getFirst100ByDetails_InQueueIsTrueAndDetails_Type_DimensionAndDetails_TypeIdentAndDetails_Type_Wanted(
							dimension, typeIdent, typeWanted);
			return all.isEmpty() ? null : all.get(new Random().nextInt(all.size()));
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	@Override @Transactional
	public Boolean isPartyActive(Long partyId) {
		return getRepository().existsByIdAndMeetingActivationTimeBefore(
				partyId, new Date(System.currentTimeMillis())
		);
	}

	@Override @Transactional
	public void deleteById(Long id) {
		try {
			super.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}