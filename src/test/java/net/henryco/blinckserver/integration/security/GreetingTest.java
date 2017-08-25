package net.henryco.blinckserver.integration.security;

import net.henryco.blinckserver.testutils.HTTPTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpMethod.GET;

/**
 * @author Henry on 25/08/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingTest {

	@LocalServerPort private int port;
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		restTemplate = new TestRestTemplate();
	}

	@Test
	public void testGreeting() throws URISyntaxException {

		URI uri = HTTPTestUtils.newURI("/test", port);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type","application/json");


		ResponseEntity<String> greeting = restTemplate.exchange(
				new RequestEntity(httpHeaders, GET, uri),
				String.class
		);

		System.out.println("\n"+greeting);
		System.out.println(greeting.getStatusCodeValue());
	}



}