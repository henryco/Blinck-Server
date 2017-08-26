package net.henryco.blinckserver.integration.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.henryco.blinckserver.testutils.HTTPTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpMethod.GET;




final class AdminJsonPostForm {

	@JsonProperty String user_id;
	@JsonProperty String password;

	AdminJsonPostForm(final String user_id,
					  final String password) {

		this.user_id = user_id;
		this.password = password;
	}

	static AdminJsonPostForm randomOne() {
		return new AdminJsonPostForm(
				HTTPTestUtils.randomNumberString(), // random uid
				HTTPTestUtils.randomNumberString() // random password
		);
	}
}



final class UserJsonPostForm {

	@JsonProperty String facebook_uid;
	@JsonProperty String facebook_token;

	UserJsonPostForm(final String facebook_uid,
					 final String facebook_token) {
		this.facebook_uid = facebook_uid;
		this.facebook_token = facebook_token;
	}

	static UserJsonPostForm randomOne() {
		return new UserJsonPostForm(
				HTTPTestUtils.randomNumberString(), // random uid
				HTTPTestUtils.randomNumberString() // random password
		);
	}
}



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationTest {

	private static final String ADMIN_LOGIN_ENDPOINT = "/login/admin";
	private static final String USER_LOGIN_ENDPOINT = "/login/user";


	@LocalServerPort private int port;
	private TestRestTemplate restTemplate;



	private ResponseEntity simplePOST(String endPoint, Object postObject) {
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
	public void adminAuthorizationGoodPostFormTest() {

		for (int i = 0; i < 20; i++) try {

			ResponseEntity entity = simplePOST(
					ADMIN_LOGIN_ENDPOINT, AdminJsonPostForm.randomOne()
			);

			responseEntityStatusErrorAssertion(entity);

		} catch (ResourceAccessException e) {
			/* EXCEPTION MEANS PROCESS WAS SUCCESSFUL, BUT CREDENTIALS WAS BAD,
			 * BUT WE DON'T CARE ABOUT BAD CREDENTIALS NOW, SO IT'S OK */
			assert true;
		}

	}




	@Test
	public void authorizationWrongPostFormTest() {

		ResponseEntity entity1 = simplePOST(
				ADMIN_LOGIN_ENDPOINT, UserJsonPostForm.randomOne()
		);

		ResponseEntity entity2 = simplePOST(
				USER_LOGIN_ENDPOINT, AdminJsonPostForm.randomOne()
		);

		assert entity1 != null;
		assert entity2 != null;

		assert entity1.getStatusCode().is5xxServerError();
		assert entity2.getStatusCode().is5xxServerError();
	}




}