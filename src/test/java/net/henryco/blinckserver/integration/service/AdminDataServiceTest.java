package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @author Henry on 01/09/17.
 */
public class AdminDataServiceTest extends BlinckIntegrationTest {


	private @Autowired AdminDataService adminDataService;



	@Test
	public void saveAdminProfileTest() throws Exception {

		Method saveProfile = BlinckTestUtil.getMethod(
				AdminDataService.class,
				"saveProfile"
		);

		final String name = "someAdmin";
		final String pass = "somePassword";
		saveProfile.invoke(adminDataService, name, pass);

		System.out.println(adminDataService.getAdminProfiles());

		adminDataService.activateProfile(name);
		System.out.println(adminDataService.getAdminProfiles());
	}



}