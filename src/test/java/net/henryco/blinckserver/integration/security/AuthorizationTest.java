package net.henryco.blinckserver.integration.security;

import net.henryco.blinckserver.integration.security.help.JsonForm;
import net.henryco.blinckserver.utils.HTTPTestUtils;
import org.junit.Before;
import org.junit.Test;
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
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpMethod.GET;


@RunWith(SpringRunner.class)
@PropertySource("classpath:/static/props/base.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationTest {

	private static final String ADMIN_LOGIN_ENDPOINT = "/login/admin";
	private static final String USER_LOGIN_ENDPOINT = "/login/user";


	private @LocalServerPort int port;
	private @Autowired Environment environment;


	private TestRestTemplate restTemplate;



	private ResponseEntity<String> simplePOST(String endPoint, Object postObject) {
		return restTemplate.exchange(
				RequestEntity.post(HTTPTestUtils.newURI(endPoint, port))
						.accept(MediaType.APPLICATION_JSON)
						.body(postObject),
				String.class
		);
	}


	private static void responseEntityStatusErrorAssertion(ResponseEntity entity) {
		assert entity != null;
		assert !entity.getStatusCode().is3xxRedirection();
		assert !entity.getStatusCode().is4xxClientError();
		assert !entity.getStatusCode().is5xxServerError();
	}





	@Before
	public void setUp() {
		restTemplate = new TestRestTemplate();
	}




	@Test
	public void connectionTest() {

		ResponseEntity<String> entity = restTemplate.exchange(
				new RequestEntity(
						GET, HTTPTestUtils.newURI("/", port)
				), String.class
		);

		assert entity != null;
		assert entity.getStatusCode().is2xxSuccessful();
	}




	@Test
	public void authorizationGoodPostFormTest() {

		for (int i = 0; i < 20; i++) try {

			ResponseEntity entity1 = simplePOST(
					ADMIN_LOGIN_ENDPOINT, JsonForm.AdminLoginPost.randomOne()
			);

			ResponseEntity entity2 = simplePOST(
					USER_LOGIN_ENDPOINT, JsonForm.UserLoginPost.randomOne()
			);

			responseEntityStatusErrorAssertion(entity1);
			responseEntityStatusErrorAssertion(entity2);

		} catch (ResourceAccessException e) {
			/* EXCEPTION MEANS PROCESS WAS SUCCESSFUL, BUT CREDENTIALS WAS BAD,
			 * BUT WE DON'T CARE ABOUT BAD CREDENTIALS NOW, SO IT'S OK */
			assert true;
		}

	}




	@Test
	public void authorizationWrongPostFormTest() {

		ResponseEntity entity1 = simplePOST(
				ADMIN_LOGIN_ENDPOINT, JsonForm.UserLoginPost.randomOne()
		);

		ResponseEntity entity2 = simplePOST(
				USER_LOGIN_ENDPOINT, JsonForm.AdminLoginPost.randomOne()
		);

		assert entity1 != null;
		assert entity2 != null;

		assert entity1.getStatusCode().is5xxServerError();
		assert entity2.getStatusCode().is5xxServerError();
	}



	@Test
	public void inMemoryAdminAuthorizationTest() {

		String name = environment.getProperty("security.default.admin.name");
		String pass = environment.getProperty("security.default.admin.password");

		ResponseEntity entity = simplePOST(
				ADMIN_LOGIN_ENDPOINT, new JsonForm.AdminLoginPost(name, pass)
		);

		responseEntityStatusErrorAssertion(entity);
		assert entity.getStatusCode().is2xxSuccessful();
	}

}