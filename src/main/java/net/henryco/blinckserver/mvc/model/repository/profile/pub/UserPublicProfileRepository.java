package net.henryco.blinckserver.mvc.model.repository.profile.pub;

import net.henryco.blinckserver.mvc.model.entity.profile.pub.UserPublicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 30/08/17.
 */
public interface UserPublicProfileRepository extends JpaRepository<UserPublicProfile, Long> {

}