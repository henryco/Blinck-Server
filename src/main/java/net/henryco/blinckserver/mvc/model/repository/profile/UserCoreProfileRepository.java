package net.henryco.blinckserver.mvc.model.repository.profile;

import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 23/08/17.
 */
public interface UserCoreProfileRepository extends JpaRepository<UserCoreProfile, Long> {
}
