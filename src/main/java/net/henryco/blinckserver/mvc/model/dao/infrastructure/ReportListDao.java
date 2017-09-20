package net.henryco.blinckserver.mvc.model.dao.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
public interface ReportListDao extends BlinckDaoTemplate<ReportList, Long> {

	List<ReportList> getForAll(int page, int size);
	List<ReportList> getAllForUser(Long userId);
	boolean existsByReporterAndReported(Long reporter, Long reported);
	long countByReported(Long reported);
	void deleteAllForUser(Long userId);
}