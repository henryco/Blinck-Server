package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * @author Henry on 27/08/17.
 */

public class PublicControllerTest extends BlinckIntegrationTest {

	private static final String FACEBOOK_PERMISSIONS_ENDPOINT = "/public/facebook/permissions";


	@Test
	public void facebookPermissionsListTest() {

		final URI uri = TestUtils.newURI(FACEBOOK_PERMISSIONS_ENDPOINT, port);
		List<String> strings = Arrays.asList(restTemplate.getForEntity(uri, String[].class).getBody());

		assert strings.contains("read_custom_friendlists");
		assert strings.contains("user_education_history");
		assert strings.contains("user_about_me");
		assert strings.contains("user_birthday");
		assert strings.contains("user_location");
		assert strings.contains("user_friends");
		assert strings.contains("user_photos");
		assert strings.contains("user_likes");

	}

}