package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckIntegrationAccessTest;
import net.henryco.blinckserver.mvc.service.data.UserDataService;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.junit.Test;
import org.springframework.social.facebook.api.User;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author Henry on 04/09/17.
 */
public class FriendshipControllerTest extends BlinckIntegrationAccessTest {

	private static final String FRIENDSHIP_ENDPOINT = "/protected/user/friends";
	private static final String FRIENDS_COUNT = FRIENDSHIP_ENDPOINT + "/count";


	private static final class TestNotification
			implements Serializable {

		public Long notification_id;
		public Date timestamp;
		public Long from;
		public Long to;

		@Override
		public String toString() {
			return "{" +
					"notification_id=" + notification_id +
					", timestamp=" + timestamp +
					", from=" + from +
					", to=" + to +
			'}';
		}
	}




	private static
	User[] createNewRandomUsers(UserDataService dataService, int numb) {

		User[] users = new User[numb];
		for (int i = 0; i < numb; i++) {
			users[i] = MockFacebookUser.getInstance().createRandom().getUser();
			dataService.addNewFacebookUserIfNotExist(users[i]);
		}
		return users;
	}




	@Test
	public void createRandomUsersTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 3);
		String[] tokens = new String[3];

		for (int i = 0; i < 3; i++)
			tokens[i] = getForUserAuthToken(users[i]);

		for (String token: tokens) {
			assert FORBIDDEN.value() != authorizedGetRequest(randomUserPath(), token).getStatusCode().value();
			assert FORBIDDEN.value() != authorizedGetRequest(FRIENDS_COUNT, token).getStatusCode().value();
		}
	}



	@Test
	public void addFriendNotificationTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 4);
		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[2]);
		String token3 = getForUserAuthToken(users[3]);

		String addRequest = FRIENDSHIP_ENDPOINT + "/add/";

		assert authorizedGetRequest(addRequest + users[0].getId(), token2).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(addRequest + users[0].getId(), token3).getStatusCode().is2xxSuccessful();

		String listRequest = FRIENDSHIP_ENDPOINT + "/request/list/income/0/100";
		TestNotification[] body = authorizedGetRequest(listRequest, token1, TestNotification[].class).getBody();

		assert body[0].timestamp.after(body[1].timestamp);
		assert body[0].to.equals(Long.decode(users[0].getId()));
		assert body[1].to.equals(Long.decode(users[0].getId()));
		assert body[0].from.equals(Long.decode(users[3].getId()));
		assert body[1].from.equals(Long.decode(users[2].getId()));
	}
}