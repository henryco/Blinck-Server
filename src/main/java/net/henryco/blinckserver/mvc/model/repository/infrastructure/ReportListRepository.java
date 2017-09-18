package net.henryco.blinckserver.mvc.model.repository.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Henry on 28/08/17.
 */
public interface ReportListRepository extends JpaRepository<ReportList, Long> {

	boolean existsByReportedIdAndReporterId(Long reportedId, Long reporterId);
	long countAllByReportedId(Long reportedId);

}