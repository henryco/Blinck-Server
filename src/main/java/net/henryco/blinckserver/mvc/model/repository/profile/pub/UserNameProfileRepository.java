package net.henryco.blinckserver.mvc.model.repository.profile.pub;

import net.henryco.blinckserver.mvc.model.entity.profile.pub.UserNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 23/08/17.
 */
public interface UserNameProfileRepository extends JpaRepository<UserNameEntity, Long> {
}
