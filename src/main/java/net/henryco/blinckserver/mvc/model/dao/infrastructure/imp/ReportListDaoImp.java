package net.henryco.blinckserver.mvc.model.dao.infrastructure.imp;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.ReportListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import org.springframework.stereotype.Repository;

/**
 * @author Henry on 29/08/17.
 */
@Repository
public class ReportListDaoImp implements ReportListDao {
	@Override
	public ReportList getById(Long id) {
		return null;
	}

	@Override
	public ReportList save(ReportList reportList) {
		return null;
	}

	@Override
	public boolean isExists(Long id) {
		return false;
	}

	@Override
	public void deleteById(Long id) {

	}
}