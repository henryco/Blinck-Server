package net.henryco.blinckserver.mvc.model.repository.relations.group;

import net.henryco.blinckserver.mvc.model.entity.relations.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
}