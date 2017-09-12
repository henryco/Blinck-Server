package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface SubPartyRepository extends JpaRepository<SubParty, Long> {

	List<SubParty> getAllByUsersIsContaining(Long user);
	List<SubParty> getAllByType_Wanted(String type_wanted);
}