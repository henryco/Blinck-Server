package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	List<Friendship> getAllByUser1OrUser2OrderByDateDesc(Long user1, Long user2);

}