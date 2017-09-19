package net.henryco.blinckserver.mvc.model.repository.relation.queue;

import net.henryco.blinckserver.mvc.model.entity.relation.queue.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 19/09/17.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

	List<Vote> getAllByTopic(String topic);
	boolean existsByTopicAndVoter(String topic, String voter);
	long countAllByTopic(String topic);
	long countAllByTopicAndVoice(String topic, Boolean voice);
	void deleteAllByTopic(String topic);

}