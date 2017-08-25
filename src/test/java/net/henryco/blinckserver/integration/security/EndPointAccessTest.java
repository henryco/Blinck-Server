package net.henryco.blinckserver.integration.security;

import net.henryco.blinckserver.testutils.HTTPTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.net.URI;

import static org.springframework.http.HttpMethod.GET;

/**
 * @author Henry on 25/08/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndPointAccessTest {

	@LocalServerPort private int port;
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		restTemplate = new TestRestTemplate();
	}


	private ResponseEntity simpleGetRequest(String endPoint, int port) {
		return restTemplate.exchange(
				new RequestEntity(
						GET, HTTPTestUtils.newURI(endPoint, port)
				), String.class
		);
	}



	@Test
	public void testRootEndPointAccess() {

		ResponseEntity response = simpleGetRequest("/", port);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	@Test
	public void testPublicEndPointAccess() {

		ResponseEntity response = simpleGetRequest("/public", port);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	@Test
	public void testProtectedEndPointAccess() {

		ResponseEntity response = simpleGetRequest("/protected", port);
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}


}