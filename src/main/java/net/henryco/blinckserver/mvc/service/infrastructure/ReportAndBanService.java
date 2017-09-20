package net.henryco.blinckserver.mvc.service.infrastructure;

import net.henryco.blinckserver.mvc.model.dao.infrastructure.ReportListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.mvc.service.profile.UserAuthProfileService;
import net.henryco.blinckserver.security.token.service.SessionWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 19/09/17.
 */
@Service
public class ReportAndBanService {

	private static final Long NUMBER_OF_REPORTS_BEFORE_BAN = 100L;

	private final ReportListDao reportListDao;
	private final UserAuthProfileService authProfileService;
	private final SessionWhiteListService whiteListService;

	@Autowired
	public ReportAndBanService(ReportListDao reportListDao,
							   UserAuthProfileService authProfileService,
							   SessionWhiteListService whiteListService) {

		this.reportListDao = reportListDao;
		this.authProfileService = authProfileService;
		this.whiteListService = whiteListService;
	}


	private static ReportList
	createReport(Long user, Long target, String reason) {

		ReportList reportList = new ReportList();
		reportList.setReporterId(user);
		reportList.setReportedId(target);
		reportList.setReason(reason);

		return reportList;
	}


	@Transactional
	public boolean report(Long user, Long target, String reason) {

		boolean exists = reportListDao.existsByReporterAndReported(user, target);
		if (exists) return false;

		reportListDao.save(createReport(user, target, reason));
		if (reportListDao.countByReported(target) >= NUMBER_OF_REPORTS_BEFORE_BAN) {

			authProfileService.setUserLocked(target, true);
			whiteListService.removeUserFromWhiteList(target);
		}
		return true;
	}

	@Transactional
	public Long countReportLists() {
		return reportListDao.count();
	}

	@Transactional
	public ReportList[] getReportList(int page, int size) {
		return reportListDao.getForAll(page, size).toArray(new ReportList[0]);
	}

	@Transactional
	public ReportList[] getReportListForUser(Long userId) {
		return reportListDao.getAllForUser(userId).toArray(new ReportList[0]);
	}

	@Transactional
	public void deleteAllForUser(Long userId) {
		reportListDao.deleteAllForUser(userId);
	}

}