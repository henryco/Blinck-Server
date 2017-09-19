package net.henryco.blinckserver.mvc.model.dao.relation.queue.imp;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.VoteDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.Vote;
import net.henryco.blinckserver.mvc.model.repository.relation.queue.VoteRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 19/09/17.
 */
@Repository
@Transactional
public class VoteDaoImp
		extends BlinckRepositoryProvider<Vote, Long>
		implements VoteDao{

	@Autowired
	public VoteDaoImp(VoteRepository repository) {
		super(repository);
	}

	private VoteRepository getRepository() {
		return provideRepository();
	}


	@Override
	public List<Vote> getAllByTopic(String topic) {
		return getRepository().getAllByTopic(topic);
	}

	@Override
	public boolean existsByTopicAndVoter(String topic, String voter) {
		return getRepository().existsByTopicAndVoter(topic, voter);
	}

	@Override
	public long countForTopic(String topic, Boolean voice) {
		return getRepository().countAllByTopicAndVoice(topic, voice);
	}

	@Override
	public long countForTopic(String topic) {
		return getRepository().countAllByTopic(topic);
	}

	@Override
	public void deleteAllByTopic(String topic) {
		getRepository().deleteAllByTopic(topic);
	}
}