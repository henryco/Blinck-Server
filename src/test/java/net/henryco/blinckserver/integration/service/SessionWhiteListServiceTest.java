package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.security.token.service.SessionWhiteListService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Henry on 01/09/17.
 */
public class SessionWhiteListServiceTest extends BlinckIntegrationTest {


	private @Autowired SessionWhiteListService whiteListService;


	@Test @Transactional
	public void saveUserSessionTest() {

		Long userId = Long.decode(TestUtils.randomGaussNumberString());
		whiteListService.addUserToWhiteList(userId);

		assert whiteListService.getAllSessions().stream()
				.anyMatch(session -> session.getUserId() != null &&
						session.getUserId().equals(userId));
	}


	@Test @Transactional
	public void saveAdminSessionTest() {

		String adminId = TestUtils.randomGaussNumberString();
		whiteListService.addAdminToWhiteList(adminId);

		assert whiteListService.getAllSessions().stream()
				.anyMatch(session -> session.getAdminId() != null &&
						session.getAdminId().equals(adminId));
	}


	@Test @Transactional
	public void removeUserSessionTest() {

		Long userId = Long.decode(TestUtils.randomGaussNumberString());

		whiteListService.addUserToWhiteList(userId);
		whiteListService.removeUserFromWhiteList(userId);

		assert whiteListService.getAllSessions().stream()
				.noneMatch(session -> session.getUserId() != null &&
						session.getUserId().equals(userId));
	}


	@Test @Transactional
	public void removeAdminSessionTest() {

		String adminId = TestUtils.randomGaussNumberString();

		whiteListService.addAdminToWhiteList(adminId);
		whiteListService.removeAdminFromWhiteList(adminId);

		assert whiteListService.getAllSessions().stream()
				.noneMatch(session -> session.getAdminId() != null &&
						session.getAdminId().equals(adminId));
	}


	@Test @Transactional
	public void existenceUserSessionTest() {

		Long userId = Long.decode(TestUtils.randomGaussNumberString());

		whiteListService.addUserToWhiteList(userId);
		assert whiteListService.isUserInTheWhiteList(userId);

		whiteListService.removeUserFromWhiteList(userId);
		assert !whiteListService.isUserInTheWhiteList(userId);
	}


	@Test @Transactional
	public void existenceAdminSessionTest() {

		String adminId = TestUtils.randomGaussNumberString();

		whiteListService.addAdminToWhiteList(adminId);
		assert whiteListService.isAdminInTheWhiteList(adminId);

		whiteListService.removeAdminFromWhiteList(adminId);
		assert !whiteListService.isAdminInTheWhiteList(adminId);
	}

}