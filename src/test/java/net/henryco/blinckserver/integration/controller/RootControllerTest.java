package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

/**
 * @author Henry on 27/08/17.
 */
public class RootControllerTest extends BlinckIntegrationTest {


	@Test
	public void indexPageTest() {

		ResponseEntity<String> response = fastGetRequest("/");
		assert !response.getStatusCode().is4xxClientError();
		assert !response.getStatusCode().is5xxServerError();
	}

}