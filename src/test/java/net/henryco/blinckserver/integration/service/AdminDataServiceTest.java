package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.security.AdminAuthProfile;
import net.henryco.blinckserver.mvc.model.entity.security.AdminVerificationQueue;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

		final Date t0 = new Date(System.currentTimeMillis() - 1);
		final String name = saveProfile(adminDataService);
		final Date t1 = new Date(System.currentTimeMillis() + 1);

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
	public void grantingNonExistedAuthorityTest() throws Exception {

		final String randomRole = "ROLE_"+TestUtils.randomGaussNumberString();
		final String name = saveProfile(adminDataService);

		final String[] roles = adminDataService.getProfile(name).getAuthorityArray();

		adminDataService.addAuthority(name, randomRole);

		final String[] newRoles = adminDataService.getProfile(name).getAuthorityArray();

		assert newRoles.length == roles.length + 1;
		assert Arrays.stream(newRoles).anyMatch(r -> r.equals(randomRole));
	}


	@Test @Transactional @SuppressWarnings("ConstantConditions")
	public void grantingExistedAuthorityTest() throws Exception {

		final String name = saveProfile(adminDataService);
		final String[] before = adminDataService.getProfile(name).getAuthorityArray();

		final String role = Arrays.stream(before).findFirst().get();
		assert role != null;

		adminDataService.addAuthority(name, role);

		final String[] after = adminDataService.getProfile(name).getAuthorityArray();

		assert after.length == before.length;
		assert Arrays.stream(after).anyMatch(s -> s.equals(role));
	}


	@Test @Transactional @SuppressWarnings("ConstantConditions")
	public void removeExistedAuthorityTest() throws Exception {

		final String name = saveProfile(adminDataService);
		final String[] before = adminDataService.getProfile(name).getAuthorityArray();

		final String role = Arrays.stream(before).findFirst().get();
		assert role != null;

		adminDataService.removeAuthority(name, role);

		final String[] after = adminDataService.getProfile(name).getAuthorityArray();

		assert after.length == before.length - 1;
		assert Arrays.stream(after).noneMatch(s -> s.equals(role));
	}


	@Test @Transactional @SuppressWarnings("ConstantConditions")
	public void removeNonExistedAuthorityTest() throws Exception {

		final String randomRole = TestUtils.randomGaussNumberString();
		final String name = saveProfile(adminDataService);
		final String[] before = adminDataService.getProfile(name).getAuthorityArray();

		adminDataService.removeAuthority(name, randomRole);

		final String[] after = adminDataService.getProfile(name).getAuthorityArray();

		assert before.length == after.length;
	}



	private static
	String saveProfile(AdminDataService adminDataService) throws Exception {

		String name = TestUtils.randomGaussNumberString();
		String pass = TestUtils.randomGaussNumberString();

		adminDataService.addNewProfile(name, pass);
		return name;
	}


}