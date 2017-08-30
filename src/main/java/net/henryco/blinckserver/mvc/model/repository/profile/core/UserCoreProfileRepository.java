package net.henryco.blinckserver.mvc.model.repository.profile.core;

import net.henryco.blinckserver.mvc.model.entity.profile.core.UserCoreProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 23/08/17.
 */
public interface UserCoreProfileRepository extends JpaRepository<UserCoreProfile, Long> {
}
