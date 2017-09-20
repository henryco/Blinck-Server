package net.henryco.blinckserver.mvc.model.dao.infrastructure.imp;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.ReportListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.mvc.model.repository.infrastructure.ReportListRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
@Repository @Transactional
public class ReportListDaoImp
		extends BlinckRepositoryProvider<ReportList, Long>
		implements ReportListDao {


	public ReportListDaoImp(ReportListRepository repository) {
		super(repository);
	}

	private ReportListRepository getRepository() {
		return provideRepository();
	}

	@Override
	public boolean existsByReporterAndReported(Long reporter, Long reported) {
		return getRepository().existsByReportedIdAndReporterId(reported, reporter);
	}

	@Override
	public long countByReported(Long reported) {
		return getRepository().countAllByReportedId(reported);
	}


	@Override
	public List<ReportList> getAllForUser(Long userId) {
		return getRepository().getAllByReportedId(userId);
	}

	@Override
	public void deleteAllForUser(Long userId) {
		getRepository().deleteAllByReportedId(userId);
	}

	@Override
	public List<ReportList> getForAll(int page, int size) {
		return getRepository().getAllByIdIsNotNullOrderByReportedId(new PageRequest(page, size));
	}
}