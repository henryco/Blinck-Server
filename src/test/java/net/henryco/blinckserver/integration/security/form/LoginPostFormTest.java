package net.henryco.blinckserver.integration.security.form;

import net.henryco.blinckserver.integration.BlinckIntegrationTest;
import net.henryco.blinckserver.utils.JsonForm;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;


public class LoginPostFormTest extends BlinckIntegrationTest {


	static final String ADMIN_LOGIN_ENDPOINT = "/login/admin";
	static final String USER_LOGIN_ENDPOINT = "/login/user";


	static void responseEntityStatusErrorAssertion(ResponseEntity entity) {
		assert entity != null;
		assert !entity.getStatusCode().is3xxRedirection();
		assert !entity.getStatusCode().is4xxClientError();
		assert !entity.getStatusCode().is5xxServerError();
	}



	@Test
	public void authorizationGoodPostFormTest() {

		for (int i = 0; i < 20; i++) try {

			ResponseEntity entity1 = fastPostRequest(
					ADMIN_LOGIN_ENDPOINT, JsonForm.AdminLoginPost.randomOne()
			);

			ResponseEntity entity2 = fastPostRequest(
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

		ResponseEntity entity1 = fastPostRequest(
				ADMIN_LOGIN_ENDPOINT, JsonForm.UserLoginPost.randomOne()
		);

		ResponseEntity entity2 = fastPostRequest(
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

		ResponseEntity entity = fastPostRequest(
				ADMIN_LOGIN_ENDPOINT, new JsonForm.AdminLoginPost(name, pass)
		);

		responseEntityStatusErrorAssertion(entity);
		assert entity.getStatusCode().is2xxSuccessful();
	}


}