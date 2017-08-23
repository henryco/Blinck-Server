package net.henryco.blinckserver.mvc.model.repository;

import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 23/08/17.
 */
public interface UserAuthProfileRepository extends JpaRepository<UserAuthProfile, Long> {


}