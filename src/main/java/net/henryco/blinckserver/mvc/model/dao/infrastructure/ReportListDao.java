package net.henryco.blinckserver.mvc.model.dao.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

/**
 * @author Henry on 29/08/17.
 */
public interface ReportListDao extends BlinckDaoTemplate<ReportList, Long> {

	boolean existsByReporterAndReported(Long reporter, Long reported);
	long countByReported(Long reported);

}