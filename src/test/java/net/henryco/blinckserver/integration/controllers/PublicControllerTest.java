package net.henryco.blinckserver.integration.controllers;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.utils.HTTPTestUtils;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

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

		final URI uri = HTTPTestUtils.newURI(FACEBOOK_PERMISSIONS_ENDPOINT, port);
		final ResponseEntity<String[]> response = restTemplate.getForEntity(uri, String[].class);
		final List<String> stringList = Arrays.asList(response.getBody());

		assert stringList.contains("read_custom_friendlists");
		assert stringList.contains("user_education_history");
		assert stringList.contains("user_about_me");
		assert stringList.contains("user_birthday");
		assert stringList.contains("user_location");
		assert stringList.contains("user_friends");
		assert stringList.contains("user_photos");
		assert stringList.contains("user_likes");

	}

}