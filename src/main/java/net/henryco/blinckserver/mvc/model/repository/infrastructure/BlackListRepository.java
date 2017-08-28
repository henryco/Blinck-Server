package net.henryco.blinckserver.mvc.model.repository.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

}