package net.henryco.blinckserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlinckServerApplicationTests {


	private static final String loginURI = "http://localhost:8080/login";
	private static final String loginBody = "{\n" +
			"  \"username\": \"admin\",\n" +
			"  \"password\": \"password1\"\n" +
			"}";

	private TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		restTemplate = new TestRestTemplate();
	}



}