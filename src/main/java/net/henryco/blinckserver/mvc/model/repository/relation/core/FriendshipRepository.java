package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	List<Friendship> getAllByUser1OrUser2OrderByDateDesc(Long user1, Long user2);
	List<Friendship> getAllByUser1OrUser2OrderByDateDesc(Long user1, Long user2, Pageable pageable);

	Friendship getByUser1AndUser2OrUser2AndUser1(Long user1, Long user2, Long user22, Long user12);

	void removeAllByUser1OrUser2(Long user1, Long user2);
	void removeAllByUser1AndUser2OrUser2AndUser1(Long user1, Long user2, Long user22, Long user12);
	boolean existsByUser1AndUser2OrUser2AndUser1(Long user1, Long user2, Long user22, Long user12);

	Long countAllByUser1OrUser2(Long user1, Long user2);
}