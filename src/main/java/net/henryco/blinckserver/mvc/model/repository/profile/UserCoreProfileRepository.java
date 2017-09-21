package net.henryco.blinckserver.mvc.model.repository.profile;

import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 23/08/17.
 */
public interface UserCoreProfileRepository extends JpaRepository<UserCoreProfile, Long> {

	List<UserCoreProfile> findAllByPublicProfile_Bio_UserName_NicknameLike(String username, Pageable pageable);
}
