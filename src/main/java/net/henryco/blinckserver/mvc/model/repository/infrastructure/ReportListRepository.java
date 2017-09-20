package net.henryco.blinckserver.mvc.model.repository.infrastructure;

import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface ReportListRepository extends JpaRepository<ReportList, Long> {

	List<ReportList> getAllByIdIsNotNullOrderByReportedId(Pageable pageable);
	List<ReportList> getAllByReportedId(Long reportedId);
	boolean existsByReportedIdAndReporterId(Long reportedId, Long reporterId);
	long countAllByReportedId(Long reportedId);
	void deleteAllByReportedId(Long reportedId);
}