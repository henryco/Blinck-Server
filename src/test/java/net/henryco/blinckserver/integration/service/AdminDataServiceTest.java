package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @author Henry on 01/09/17.
 */
public class AdminDataServiceTest extends BlinckIntegrationTest {


	private @Autowired AdminDataService adminDataService;



	@Test @Transactional
	public void saveAdminProfileTest() throws Exception {

		final String name = saveProfile(adminDataService);

		AdminAuthProfile profile = adminDataService.getProfile(name);
		assert profile != null;
		assert !profile.isEnabled();
	}


	@Test @Transactional
	public void deleteAdminProfileTest() throws Exception {

		Function<String, Boolean> assertion = s ->
				adminDataService.getVerificationQueue()
						.stream().anyMatch(q -> q.getId().equals(s));

		final String name = saveProfile(adminDataService);

		assert adminDataService.getProfile(name) != null;
		assert assertion.apply(name);

		adminDataService.deleteProfile(name);

		assert adminDataService.getProfile(name) == null;
		assert !assertion.apply(name);
	}


	@Test @Transactional
	public void activateAdminProfileTest() throws Exception {

		final String name = saveProfile(adminDataService);

		assert !adminDataService.getProfile(name).isEnabled();
		adminDataService.activateProfile(name);
		assert adminDataService.getProfile(name).isEnabled();
	}


	@Test @Transactional
	public void verificationQueueMutationTest() throws Exception {

		final String name = saveProfile(adminDataService);

		Function<String, Boolean> assertion = s ->
				adminDataService.getVerificationQueue().stream()
						.anyMatch(v -> v.getId().equals(s));

		assert assertion.apply(name);
		adminDataService.activateProfile(name);
		assert !assertion.apply(name);
	}


	@Test @Transactional
	public void verificationQueueDateCreationTest() throws Exception {

		final Date t0 = new Date(System.currentTimeMillis());
		final String name = saveProfile(adminDataService);
		final Date t1 = new Date(System.currentTimeMillis());

		Consumer<AdminVerificationQueue> assertion = queue -> {
			assert queue.getRegistrationTime().after(t0);
			assert queue.getRegistrationTime().before(t1);
		};

		adminDataService.getVerificationQueue().stream()
				.filter(avq -> avq.getId().equals(name))
				.findFirst()
		.ifPresent(assertion);
	}


	@Test @Transactional
	public void grantingAuthorityTest() {

	}


	private static
	String saveProfile(AdminDataService adminDataService) throws Exception {
		String name = TestUtils.randomGaussNumberString();
		String pass = TestUtils.randomGaussNumberString();
		BlinckTestUtil.getMethod(
				AdminDataService.class,
				"saveProfile"
		).invoke(adminDataService, name, pass);
		return name;
	}


}