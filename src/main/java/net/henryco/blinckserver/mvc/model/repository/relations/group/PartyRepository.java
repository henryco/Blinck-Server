package net.henryco.blinckserver.mvc.model.repository.relations.group;

import net.henryco.blinckserver.mvc.model.entity.relation.Party;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface PartyRepository extends JpaRepository<Party, Long> {
}