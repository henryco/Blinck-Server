package net.henryco.blinckserver.mvc.service.relation.queue;

import net.henryco.blinckserver.mvc.model.dao.relation.queue.VoteDao;
import net.henryco.blinckserver.mvc.model.entity.relation.queue.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author Henry on 19/09/17.
 */
@Service
public class VoteService {

	private final VoteDao voteDao;

	@Autowired
	public VoteService(VoteDao voteDao) {
		this.voteDao = voteDao;
	}


	@Transactional
	public Boolean vote(Serializable topic, Serializable voter, boolean voice) {

		return !voteDao.existsByTopicAndVoter(topic.toString(), voter.toString())
				&& voteDao.save(new Vote(topic, voter, voice)) != null;
	}

	@Transactional
	public void deleteAllForTopic(Serializable topic) {
		voteDao.deleteAllByTopic(topic.toString());
	}

	@Transactional
	public Vote[] getAllForTopic(Serializable topic) {
		return voteDao.getAllByTopic(topic.toString()).toArray(new Vote[0]);
	}

	@Transactional
	public long countForTopic(Serializable topic, Boolean voice) {
		return voteDao.countForTopic(topic.toString(), voice);
	}

	@Transactional
	public long countForTopic(Serializable topic) {
		return voteDao.countForTopic(topic.toString());
	}

}