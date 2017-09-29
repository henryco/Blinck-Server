package net.henryco.blinckserver.mvc.model.repository.security;

import net.henryco.blinckserver.mvc.model.entity.security.SessionWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 01/09/17.
 */
public interface SessionWhiteListRepository extends JpaRepository<SessionWhiteList, Long> {

	// todo: replace with DELETE ALL 
	void deleteSessionWhiteListByAdminId(String adminId);
	void deleteSessionWhiteListByUserId(Long userId);

	boolean existsByAdminId(String adminId);
	boolean existsByUserId(Long id);

}
