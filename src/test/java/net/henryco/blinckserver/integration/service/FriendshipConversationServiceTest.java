package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

/**
 * @author Henry on 05/09/17.
 */
public class FriendshipConversationServiceTest extends BlinckUserIntegrationTest {


	private @Autowired FriendshipConversationService conversationService;
	private @Autowired FriendshipService friendshipService;


	@Test @Transactional
	public void conversationTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		Long relation1 = friendshipService.addFriendshipRelation(users[0], users[1]);
		Long relation2 = friendshipService.addFriendshipRelation(users[1], users[2]);

		Long msg01 = conversationService.save(createRandomMessage(users[0], relation1)).getId();
		Long msg02 = conversationService.save(createRandomMessage(users[1], relation1)).getId();

		Long msg11 = conversationService.save(createRandomMessage(users[1], relation2)).getId();
		Long msg12 = conversationService.save(createRandomMessage(users[2], relation2)).getId();

		Long msg03 = conversationService.save(createRandomMessage(users[1], relation1)).getId();
		Long msg04 = conversationService.save(createRandomMessage(users[1], relation1)).getId();
		Long msg05 = conversationService.save(createRandomMessage(users[0], relation1)).getId();

		Long msg13 = conversationService.save(createRandomMessage(users[2], relation2)).getId();

		Long msg06 = conversationService.save(createRandomMessage(users[1], relation1)).getId();


		FriendshipConversation[] conversation1 = conversationService.getByFriendshipId(relation1)
				.toArray(new FriendshipConversation[0]);
		FriendshipConversation[] conversation2 = conversationService.getByFriendshipId(relation2)
				.toArray(new FriendshipConversation[0]);

		assert conversation1[0].getId().equals(msg06);
		assert conversation1[1].getId().equals(msg05);
		assert conversation1[2].getId().equals(msg04);
		assert conversation1[3].getId().equals(msg03);
		assert conversation1[4].getId().equals(msg02);
		assert conversation1[5].getId().equals(msg01);

		assert conversation2[0].getId().equals(msg13);
		assert conversation2[1].getId().equals(msg12);
		assert conversation2[2].getId().equals(msg11);

		for (int i = 1; i < conversation1.length; i++)
			assert conversation1[i].getDate().before(conversation1[i - 1].getDate());

		for (int i = 1; i < conversation2.length; i++)
			assert conversation2[i].getDate().before(conversation2[i - 1].getDate());
	}



	@Test @Transactional
	public void countAndRemoveConversationTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		Long relation1 = friendshipService.addFriendshipRelation(users[0], users[1]);
		Long relation2 = friendshipService.addFriendshipRelation(users[1], users[2]);

		Long[] rel1 = { users[0], users[1] };
		Long[] rel2 = { users[1], users[2] };

		for (int i = 0; i < 100; i++) {
			if (new Random().nextInt(2) == 0) {
				conversationService.save(createRandomMessage(rel1[new Random().nextInt(2)], relation1));
				conversationService.save(createRandomMessage(rel2[new Random().nextInt(2)], relation2));
			} else {
				conversationService.save(createRandomMessage(rel2[new Random().nextInt(2)], relation2));
				conversationService.save(createRandomMessage(rel1[new Random().nextInt(2)], relation1));
			}
		}

		assert conversationService.countByFriendshipId(relation1) == 100;
		assert conversationService.countByFriendshipId(relation2) == 100;

		conversationService.deleteAllByFriendshipId(relation2);
		assert conversationService.countByFriendshipId(relation1) == 100;
		assert conversationService.countByFriendshipId(relation2) == 0;

		conversationService.deleteAllByFriendshipId(relation1);
		assert conversationService.countByFriendshipId(relation1) == 0;
		assert conversationService.countByFriendshipId(relation2) == 0;
	}



	private static FriendshipConversation
	createRandomMessage(Long author, Long relation) throws Exception {

		FriendshipConversation message = new FriendshipConversation();
		message.setAuthor(author);
		message.setFriendship(relation);
		message.setMessage(TestUtils.randomGaussNumberString(1_000_000_000_000L));
		message.setDate(new Date(System.currentTimeMillis()));
		Thread.sleep(5);
		return message;
	}

}