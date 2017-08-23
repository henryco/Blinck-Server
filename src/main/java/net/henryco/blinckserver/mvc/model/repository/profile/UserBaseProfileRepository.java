package net.henryco.blinckserver.mvc.model.repository.profile;

import net.henryco.blinckserver.mvc.model.entity.profile.UserBaseProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 23/08/17.
 */
public interface UserBaseProfileRepository extends JpaRepository<UserBaseProfile, Long> {
}
