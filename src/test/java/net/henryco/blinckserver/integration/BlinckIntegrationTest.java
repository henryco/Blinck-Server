package net.henryco.blinckserver.integration;

import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.http.HttpMethod.GET;

/**
 * @author Henry on 27/08/17.
 */

@RunWith(SpringRunner.class)
@PropertySource("classpath:/static/props/base.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BlinckIntegrationTest implements TestUtils {

	protected @LocalServerPort int port;
	protected @Autowired Environment environment;
	protected TestRestTemplate restTemplate;


	protected final ResponseEntity<String> fastGetRequest(String endPoint) {
		return fastGetRequest(endPoint, String.class);
	}

	protected final <T> ResponseEntity<T> fastGetRequest(String endPoint, Class<T> entityType) {
		return restTemplate.exchange(
				new RequestEntity(
						GET, TestUtils.newURI(endPoint, port)
				), entityType
		);
	}

	protected final ResponseEntity<String> fastPostRequest(String endPoint, Object postForm) {
		return fastPostRequest(endPoint, postForm, String.class);
	}

	protected final <T> ResponseEntity<T> fastPostRequest(String endPoint, Object postForm, Class<T> entityType) {
		return restTemplate.exchange(
				RequestEntity.post(TestUtils.newURI(endPoint, port))
						.accept(MediaType.APPLICATION_JSON)
						.body(postForm),
				entityType
		);
	}


	@Before
	public final void setUpIntegrationTest() {
		restTemplate = new TestRestTemplate();
	}

}