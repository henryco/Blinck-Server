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
	private static final String FRIEND_ADD = FRIENDSHIP_ENDPOINT + "/add/";
	private static final String FRIEND_REMOVE = FRIENDSHIP_ENDPOINT + "/remove/";

	private static final String LIST_ALL = FRIENDSHIP_ENDPOINT + "/list/0/100";
	private static final String LIST_DETAILED = FRIENDSHIP_ENDPOINT + "/detailed/list/0/100";
	private static final String LIST_INCOME = FRIENDSHIP_ENDPOINT + "/request/list/income/0/100";
	private static final String LIST_OUTCOME = FRIENDSHIP_ENDPOINT + "/request/list/outcome/0/100";

	private static final String DELETE = FRIENDSHIP_ENDPOINT + "/request/delete/";
	private static final String REQUEST = FRIENDSHIP_ENDPOINT + "/request/";
	private static final String ACCEPT  = "/accept";
	private static final String DECLINE = "/decline";



	private static final class TestNotification
			implements Serializable {

		public Long notification;
		public Date timestamp;
		public Long from;
		public Long to;

		@Override
		public String toString() {
			return "{" +
					"notification=" + notification +
					", timestamp=" + timestamp +
					", from=" + from +
					", to=" + to +
			'}';
		}
	}


	private static final class TestDetailFriendship
			implements Serializable {

		public Long id;
		public Long friend;

		@Override
		public String toString() {
			return "{" +
					"id=" + id +
					", friend=" + friend +
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
	public void addFriendTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 2);
		String token1 = getForUserAuthToken(users[0]);

		assert authorizedGetRequest(FRIEND_ADD + users[1].getId(), token1).getStatusCode().is2xxSuccessful();
	}



	@Test
	public void incomeFriendNotificationTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 4);
		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[2]);
		String token3 = getForUserAuthToken(users[3]);

		assert authorizedGetRequest(FRIEND_ADD + users[0].getId(), token2).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(FRIEND_ADD + users[0].getId(), token3).getStatusCode().is2xxSuccessful();

		TestNotification[] body = authorizedGetRequest(LIST_INCOME, token1, TestNotification[].class).getBody();

		assert body[0].timestamp.after(body[1].timestamp);
		assert body[0].to.equals(Long.decode(users[0].getId()));
		assert body[1].to.equals(Long.decode(users[0].getId()));
		assert body[0].from.equals(Long.decode(users[3].getId()));
		assert body[1].from.equals(Long.decode(users[2].getId()));
	}



	@Test
	public void outcomeFriendNotificationTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 4);
		String token1 = getForUserAuthToken(users[1]);

		assert authorizedGetRequest(FRIEND_ADD + users[2].getId(), token1).getStatusCode().is2xxSuccessful();
		assert authorizedGetRequest(FRIEND_ADD + users[3].getId(), token1).getStatusCode().is2xxSuccessful();

		TestNotification[] body = authorizedGetRequest(LIST_OUTCOME, token1, TestNotification[].class).getBody();

		assert body[0].timestamp.after(body[1].timestamp);
		assert body[0].to.equals(Long.decode(users[3].getId()));
		assert body[1].to.equals(Long.decode(users[2].getId()));
		assert body[0].from.equals(Long.decode(users[1].getId()));
		assert body[1].from.equals(Long.decode(users[1].getId()));
	}



	@Test
	public void incomeListPageSizeTest() throws Exception{

		final String CUSTOM_REQUEST = FRIENDSHIP_ENDPOINT + "/request/list/income/1/3";

		User[] users = createNewRandomUsers(userDataService, 10);
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		TestNotification[] body = authorizedGetRequest(
				LIST_INCOME, getForUserAuthToken(users[0]), TestNotification[].class
		).getBody();

		TestNotification[] body2 = authorizedGetRequest(
				CUSTOM_REQUEST, getForUserAuthToken(users[0]), TestNotification[].class
		).getBody();

		assert body.length == 9;
		assert body2.length == 3;

		assert body2[0].notification.equals(body[3].notification);
		assert body2[1].notification.equals(body[4].notification);
		assert body2[2].notification.equals(body[5].notification);
	}



	@Test
	public void acceptFriendRequestTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 4);
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		String token = getForUserAuthToken(users[0]);
		TestNotification[] body = authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody();

		for (TestNotification n: body) {
			String request = REQUEST + n.from + ACCEPT;
			assert authorizedGetRequest(request, token).getStatusCode().is2xxSuccessful();
		}

		assert authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody().length == 0;
		assert authorizedGetRequest(LIST_ALL, token, Long[].class).getBody().length == 3;
	}



	@Test
	public void declineFriendRequestTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 4);
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		String token = getForUserAuthToken(users[0]);

		for (TestNotification n: authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody()) {
			String request = REQUEST + n.from + DECLINE;
			assert authorizedGetRequest(request, token).getStatusCode().is2xxSuccessful();
		}

		assert authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody().length == 0;
		assert authorizedGetRequest(LIST_ALL, token, Long[].class).getBody().length == 0;
	}



	@Test
	public void deleteRequestTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 3);
		String token1 = getForUserAuthToken(users[0]);

		for (User user: users) authorizedGetRequest(FRIEND_ADD + user.getId(), token1);

		TestNotification[] body = authorizedGetRequest(LIST_OUTCOME, token1, TestNotification[].class).getBody();
		assert body.length == 2;

		authorizedGetRequest(DELETE + body[0].notification, token1);

		TestNotification[] body2 = authorizedGetRequest(LIST_OUTCOME, token1, TestNotification[].class).getBody();
		assert body2.length == 1;
		assert !body2[0].notification.equals(body[0].notification);

		authorizedGetRequest(DELETE + body2[0].notification, getForUserAuthToken(users[1]));

		TestNotification[] body3 = authorizedGetRequest(LIST_OUTCOME, token1, TestNotification[].class).getBody();
		assert body3.length == 1; // ONLY AUTHOR OF NOTIFICATION CAN DIRECTLY DELETE IT
		assert body3[0].notification.equals(body2[0].notification);
	}



	@Test
	public void friendListAndCountTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 17);
		Long userId = Long.decode(users[0].getId());
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		String token = getForUserAuthToken(users[0]);
		assert authorizedGetRequest(LIST_ALL, token, Long[].class).getBody().length == 0;

		for (TestNotification n: authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody()) {
			authorizedGetRequest(REQUEST + n.from + ACCEPT, token);
		}

		assert authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody().length == 0;

		Long[] friends = authorizedGetRequest(LIST_ALL, token, Long[].class).getBody();

		assert friends.length == 16;
		assert Arrays.stream(friends).noneMatch(userId::equals);

		Long count = authorizedGetRequest(FRIENDS_COUNT, token, Long.class).getBody();
		assert count.equals((long) friends.length);
	}



	@Test
	public void removeFriendTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 5);
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		String token = getForUserAuthToken(users[0]);
		String secondToken = getForUserAuthToken(users[1]);

		for (TestNotification n: authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody()) {
			authorizedGetRequest(REQUEST + n.from + ACCEPT, token);
		}


		Long[] friendsBefore = authorizedGetRequest(LIST_ALL, token, Long[].class).getBody();
		assert Arrays.stream(friendsBefore).anyMatch(f -> f.equals(Long.decode(users[1].getId())));


		Long[] secondListBefore = authorizedGetRequest(LIST_ALL, secondToken, Long[].class).getBody();
		assert Arrays.stream(secondListBefore).anyMatch(f -> f.equals(Long.decode(users[0].getId())));


		authorizedGetRequest(FRIEND_REMOVE + users[3].getId(), token);
		authorizedGetRequest(FRIEND_REMOVE + users[1].getId(), token);


		Long[] friendsAfter = authorizedGetRequest(LIST_ALL, token, Long[].class).getBody();
		assert friendsAfter.length == 2;
		assert Arrays.stream(friendsAfter).noneMatch(f -> f.equals(Long.decode(users[1].getId())));
		assert Arrays.stream(friendsAfter).noneMatch(f -> f.equals(Long.decode(users[3].getId())));
		assert Arrays.stream(friendsAfter).anyMatch(f -> f.equals(Long.decode(users[2].getId())));
		assert Arrays.stream(friendsAfter).anyMatch(f -> f.equals(Long.decode(users[4].getId())));


		Long[] secondListAfter = authorizedGetRequest(LIST_ALL, secondToken, Long[].class).getBody();
		assert Arrays.stream(secondListAfter).noneMatch(f -> f.equals(Long.decode(users[0].getId())));
	}



	@Test
	public void detailedFriendListTest() throws Exception {

		User[] users = createNewRandomUsers(userDataService, 8);
		for (User user: users) {
			authorizedGetRequest(FRIEND_ADD + users[0].getId(), getForUserAuthToken(user));
		}

		String token = getForUserAuthToken(users[0]);

		for (TestNotification n: authorizedGetRequest(LIST_INCOME, token, TestNotification[].class).getBody()) {
			authorizedGetRequest(REQUEST + n.from + ACCEPT, token);
		}

		TestDetailFriendship[] body = authorizedGetRequest(LIST_DETAILED, token, TestDetailFriendship[].class).getBody();
		for (TestDetailFriendship d: body) {
			System.out.println(d);
		}

	}

}