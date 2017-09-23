package net.henryco.blinckserver.integration.service;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.FriendshipConversation;
import net.henryco.blinckserver.mvc.model.entity.relation.conversation.embeded.MessagePart;
import net.henryco.blinckserver.mvc.service.relation.conversation.FriendshipConversationService;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import net.henryco.blinckserver.utils.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

import static net.henryco.blinckserver.mvc.service.relation.conversation.ConversationService.MessageForm;

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

		conversationService.save(createRandomConversation(users[0], relation1));
		conversationService.save(createRandomConversation(users[1], relation1));

		conversationService.save(createRandomConversation(users[1], relation2));
		conversationService.save(createRandomConversation(users[2], relation2));

		conversationService.save(createRandomConversation(users[1], relation1));
		conversationService.save(createRandomConversation(users[1], relation1));
		conversationService.save(createRandomConversation(users[0], relation1));

		conversationService.save(createRandomConversation(users[2], relation2));

		conversationService.save(createRandomConversation(users[1], relation1));

		MessageForm[] conversation1 = conversationService.getLastNByTopic(relation1, 0, Integer.MAX_VALUE);
		MessageForm[] conversation2 = conversationService.getLastNByTopic(relation2, 0, Integer.MAX_VALUE);

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

			Long author1 = rel1[new Random().nextInt(2)];
			MessageForm random1 = createRandomMessage(author1, relation1);
			Long author2 = rel2[new Random().nextInt(2)];
			MessageForm random2 = createRandomMessage(author2, relation2);

			if (new Random().nextInt(2) == 0) {

				conversationService.sendMessage(random1, author1);
				conversationService.sendMessage(random2, author2);

			} else {

				conversationService.sendMessage(random2, author2);
				conversationService.sendMessage(random1, author1);
			}
		}

		assert conversationService.countByTopic(relation1) == 100;
		assert conversationService.countByTopic(relation2) == 100;

		conversationService.deleteAllInTopic(relation2);
		assert conversationService.countByTopic(relation1) == 100;
		assert conversationService.countByTopic(relation2) == 0;

		conversationService.deleteAllInTopic(relation1);
		assert conversationService.countByTopic(relation1) == 0;
		assert conversationService.countByTopic(relation2) == 0;
	}


	private static MessageForm
	createRandomMessage(Long author, Long relation) throws Exception {

		MessageForm form = new MessageForm();
		form.setAuthor(author);
		form.setTopic(relation);
		form.setMessage(TestUtils.randomGaussNumberString(1_000_000_000_000L));
		form.setDate(new Date(System.currentTimeMillis()));
		Thread.sleep(5);
		return form;
	}

	private static FriendshipConversation
	createRandomConversation(Long author, Long relation) throws Exception {

		FriendshipConversation conversation = new FriendshipConversation();
		conversation.setFriendship(relation);

		MessagePart part = new MessagePart();
		part.setAuthor(author);
		part.setMessage(TestUtils.randomGaussNumberString(1_000_000_000_000L));
		part.setDate(new Date(System.currentTimeMillis()));

		conversation.setMessagePart(part);
		Thread.sleep(5);
		return conversation;
	}

}