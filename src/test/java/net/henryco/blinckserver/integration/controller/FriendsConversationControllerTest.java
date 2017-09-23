package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author Henry on 06/09/17.
 */
public class FriendsConversationControllerTest extends BlinckUserIntegrationTest {


	private static final String CONVERSATION = "/protected/user/friends/conversation";
	private static final String MESSAGES = CONVERSATION + "/messages";
	private static final String DELETE = CONVERSATION + "/remove?id=";

	private static final String SEND = MESSAGES + "/send";
	private static final String COUNT = MESSAGES + "/count?id=";
	private static final String LAST = MESSAGES + "/last?id=";
	private static final String LIST = MESSAGES + "/list?page=0&size=100&id=";


	private static class TestMessagePost
			implements Serializable {

		public Long topic;
		public Long author;
		public String message;
		public Date timestamp;

		@Override
		public String toString() {
			return "{" +
					"topic=" + topic +
					", message='" + message + '\'' +
					", author=" + author +
					", timestamp=" + timestamp +
					'}';
		}
	}



	private static
	TestMessagePost createRandomPost(Long relation) {
		TestMessagePost post = new TestMessagePost();
		post.message = TestUtils.randomGaussNumberString(100000000000000000L);
		post.topic = relation;
		return post;
	}




	private @Autowired FriendshipService friendshipService;



	@Test
	public void sendAndCountMessagesTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[1]);
		String token3 = getForUserAuthToken(users[2]);

		Long relation = friendshipService.addFriendshipRelation(users[0], users[1]);

		authorizedPostRequest(SEND, token1, createRandomPost(relation)); // 1
		authorizedPostRequest(SEND, token1, createRandomPost(relation)); // 2
		authorizedPostRequest(SEND, token2, createRandomPost(relation)); // 3
		authorizedPostRequest(SEND, token1, createRandomPost(relation)); // 4
		authorizedPostRequest(SEND, token2, createRandomPost(relation)); // 5

		authorizedPostRequest(SEND, token3, createRandomPost(relation)); // <-- this post must be ignored by server

		assert authorizedGetRequest(COUNT + relation, token1, Long.class).getBody().equals(5L);
	}



	@Test
	public void lastMessageTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[1]);
		String token3 = getForUserAuthToken(users[2]);
		Long relation = friendshipService.addFriendshipRelation(users[0], users[1]);

		authorizedPostRequest(SEND, token1, createRandomPost(relation));
		authorizedPostRequest(SEND, token1, createRandomPost(relation));

		TestMessagePost randomPost = createRandomPost(relation);
		authorizedPostRequest(SEND, token2, randomPost);

		TestMessagePost body = authorizedGetRequest(LAST + relation, token1, TestMessagePost.class).getBody();
		assert body.message.equals(randomPost.message);
		assert body.topic.equals(randomPost.topic);
		assert body.author.equals(users[1]);

		// access denied because only conversation members (friends) can read and write messages
		assert authorizedGetRequest(LAST + relation, token3, TestMessagePost.class).getStatusCode().is4xxClientError();
	}



	@Test
	public void listOfMessagesTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 4);
		Long relation1 = friendshipService.addFriendshipRelation(users[0], users[1]);
		Long relation2 = friendshipService.addFriendshipRelation(users[0], users[2]);
		Long relation3 = friendshipService.addFriendshipRelation(users[1], users[3]);

		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[1]);
		String token3 = getForUserAuthToken(users[2]);
		String token4 = getForUserAuthToken(users[3]);

		for (int i = 0; i < 5; i++) { // 2 * 5 == 10
			authorizedPostRequest(SEND, token1, createRandomPost(relation1));
			authorizedPostRequest(SEND, token2, createRandomPost(relation1));
		}

		for (int i = 0; i < 2; i++) { // 2 * 2 == 4
			authorizedPostRequest(SEND, token3, createRandomPost(relation2));
			authorizedPostRequest(SEND, token1, createRandomPost(relation2));
		}

		for (int i = 0; i < 6; i++) { // 2 * 6 == 12
			authorizedPostRequest(SEND, token2, createRandomPost(relation3));
			authorizedPostRequest(SEND, token4, createRandomPost(relation3));
		}


		assert authorizedGetRequest(COUNT + relation1, token1, Long.class).getBody().equals(10L);
		assert authorizedGetRequest(COUNT + relation2, token3, Long.class).getBody().equals(4L);
		assert authorizedGetRequest(COUNT + relation3, token2, Long.class).getBody().equals(12L);


		TestMessagePost[] res1 = authorizedGetRequest(LIST + relation1, token2, TestMessagePost[].class).getBody();
		TestMessagePost[] res2 = authorizedGetRequest(LIST + relation2, token3, TestMessagePost[].class).getBody();
		TestMessagePost[] res3 = authorizedGetRequest(LIST + relation3, token4, TestMessagePost[].class).getBody();

		assert res1.length == 10;
		assert res2.length == 4;
		assert res3.length == 12;

		Consumer<TestMessagePost[]> assertion = res -> {
			for (int i = 1; i < res.length; i++) {
				assert res[i].timestamp.before(res[i - 1].timestamp);
			}
		};

		assertion.accept(res1);
		assertion.accept(res2);
		assertion.accept(res3);

		assert Arrays.stream(res1).noneMatch(r -> r.author.equals(users[2]) || r.author.equals(users[3]));
		assert Arrays.stream(res2).noneMatch(r -> r.author.equals(users[1]) || r.author.equals(users[3]));
		assert Arrays.stream(res3).noneMatch(r -> r.author.equals(users[0]) || r.author.equals(users[2]));

		assert Arrays.stream(res1).anyMatch(r -> r.author.equals(users[0]) || r.author.equals(users[1]));
		assert Arrays.stream(res2).anyMatch(r -> r.author.equals(users[0]) || r.author.equals(users[2]));
		assert Arrays.stream(res3).anyMatch(r -> r.author.equals(users[1]) || r.author.equals(users[3]));

		// only conversation (friendship) members can read messages
		assert authorizedGetRequest(COUNT + relation1, token4).getStatusCode().is4xxClientError();
	}



	@Test
	public void deleteMessagesTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		Long relation1 = friendshipService.addFriendshipRelation(users[0], users[1]);
		Long relation2 = friendshipService.addFriendshipRelation(users[0], users[2]);

		String token1 = getForUserAuthToken(users[0]);
		String token2 = getForUserAuthToken(users[1]);
		String token3 = getForUserAuthToken(users[2]);

		for (int i = 0; i < 5; i++) {
			authorizedPostRequest(SEND, token2, createRandomPost(relation1));
			authorizedPostRequest(SEND, token1, createRandomPost(relation1));
			authorizedPostRequest(SEND, token3, createRandomPost(relation2));
			authorizedPostRequest(SEND, token1, createRandomPost(relation2));
			authorizedPostRequest(SEND, token1, createRandomPost(relation1));
		}

		assert authorizedGetRequest(COUNT + relation1, token1, Long.class).getBody() != 0;
		assert authorizedGetRequest(COUNT + relation2, token1, Long.class).getBody() != 0;

		authorizedDeleteRequest(DELETE + relation1, token1);
		assert authorizedGetRequest(COUNT + relation1, token1, Long.class).getBody() == 0;
		assert authorizedGetRequest(COUNT + relation2, token1, Long.class).getBody() != 0;

		authorizedDeleteRequest(DELETE + relation2, token2); // this user can't delete so server will ignore
		assert authorizedGetRequest(COUNT + relation1, token1, Long.class).getBody() == 0;
		assert authorizedGetRequest(COUNT + relation2, token1, Long.class).getBody() != 0;

		authorizedDeleteRequest(DELETE + relation2, token3); // NOW OK
		assert authorizedGetRequest(COUNT + relation1, token1, Long.class).getBody() == 0;
		assert authorizedGetRequest(COUNT + relation2, token1, Long.class).getBody() == 0;
	}


}