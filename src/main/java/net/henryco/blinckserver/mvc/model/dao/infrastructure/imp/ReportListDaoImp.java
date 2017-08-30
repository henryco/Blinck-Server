package net.henryco.blinckserver.mvc.model.dao.infrastructure.imp;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.ReportListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.mvc.model.repository.infrastructure.ReportListRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class ReportListDaoImp
		extends BlinckRepositoryProvider<ReportList, Long>
		implements ReportListDao {


	public ReportListDaoImp(ReportListRepository repository) {
		super(repository);
	}


}