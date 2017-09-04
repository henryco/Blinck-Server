package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.BlackListDao;
import net.henryco.blinckserver.mvc.model.dao.infrastructure.ReportListDao;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.BlackList;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Henry on 02/09/17.
 */
public class ReportAndBlackListEntityTest extends BlinckUserIntegrationTest {


	private @Autowired ReportListDao reportListDao;
	private @Autowired BlackListDao blackListDao;


	@Test @Transactional
	public void addToReportListTest() {

		Long[] ids = saveNewRandomUsers(this);

		long id1 = ids[0];
		long id2 = ids[1];

		assert reportListDao.isExists(addRandomReport(this, id1, id2));

		reportListDao.getAll().forEach(rp -> {
			assert coreProfileDao.isExists(rp.getReportedId());
			assert coreProfileDao.isExists(rp.getReporterId());
		});
	}


	@Test @Transactional
	public void addToBlackListTest() {

		Long[] ids = saveNewRandomUsers(this);

		long id1 = ids[0];
		long id2 = ids[1];

		assert blackListDao.isExists(addToBlackList(this, id1, id2));

		blackListDao.getAll().forEach(bl -> {
			assert coreProfileDao.isExists(bl.getUserId());
			assert coreProfileDao.isExists(bl.getBlockedUserId());
		});
	}


	@Test @Transactional
	public void removeFromReportListTest() {

		Long[] ids = saveNewRandomUsers(this);

		long id1 = ids[0];
		long id2 = ids[1];

		Long reportId = addRandomReport(this, id1, id2);
		assert reportListDao.isExists(reportId);

		reportListDao.deleteById(reportId);
		assert !reportListDao.isExists(reportId);

		coreProfileDao.getAll().forEach(profile -> {
			assert reportListDao.getAll().stream().noneMatch(
					rp -> rp.getReportedId() == profile.getId() || rp.getReporterId() == profile.getId());
		});
	}


	@Test @Transactional
	public void removeFromBlackListTest() {

		Long[] ids = saveNewRandomUsers(this);

		long id1 = ids[0];
		long id2 = ids[1];

		Long blId = addToBlackList(this, id1, id2);
		assert blackListDao.isExists(blId);

		blackListDao.deleteById(blId);
		assert !blackListDao.isExists(blId);

		coreProfileDao.getAll().forEach(profile -> {
			assert blackListDao.getAll().stream().noneMatch(
					bk -> bk.getBlockedUserId() == profile.getId() || bk.getUserId() == profile.getId()
			);
		});
	}



	private static Long
	addRandomReport(ReportAndBlackListEntityTest context, Long id1, Long id2) {

		ReportList reportList = new ReportList();
		reportList.setReason(TestUtils.randomGaussNumberString());
		reportList.setReporterId(id1);
		reportList.setReportedId(id2);
		return context.reportListDao.save(reportList).getId();
	}

	private static Long
	addToBlackList(ReportAndBlackListEntityTest context, Long id1, Long id2) {

		BlackList blackList = new BlackList();
		blackList.setUserId(id1);
		blackList.setBlockedUserId(id2);
		return context.blackListDao.save(blackList).getId();
	}


}