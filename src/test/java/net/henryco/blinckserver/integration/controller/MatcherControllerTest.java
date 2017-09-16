package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckStompIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.service.relation.core.FriendshipService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Henry on 15/09/17.
 */
public class MatcherControllerTest extends BlinckStompIntegrationTest {

	private static final String MONITOR_SUBPARTY = "/protected/admin/monitor/subparty/all";
	private static final String MONITOR_PARTY = "/protected/admin/monitor/party/all";

	private static final String MATCH = "/protected/user/match";
	private static final String QUEUE = MATCH + "/queue";
	private static final String SOLO = QUEUE + "/solo";
	private static final String CUSTOM = QUEUE + "/custom";

	private static final String LIST = QUEUE + "/list";
	private static final String LEAVE = QUEUE + "/leave?id=";

	private static final String JOIN = CUSTOM + "/join?id=";
	private static final String CUSTOM_START = CUSTOM + "/start?id=";
	private static final String CUSTOM_LEAVE = CUSTOM + "/leave?id=";
	private static final String CUSTOM_LIST = CUSTOM + "/list";
	private static final String CUSTOM_REMOVE = CUSTOM + "/delete?id=";


	private @Autowired FriendshipService friendshipService;

	private static final class TestTypeForm
			implements Serializable {

		public static final String TYPE_MALE = "male";
		public static final String TYPE_FEMALE = "female";
		public static final String TYPE_BOTH = "both";

		public String ident;
		public String wanted;
		public Integer dimension;
	}

	private static
	TestTypeForm newDefaultForm(int dim) {

		TestTypeForm typeForm = new TestTypeForm();
		typeForm.dimension = dim;
		typeForm.ident = TestTypeForm.TYPE_BOTH;
		typeForm.wanted = TestTypeForm.TYPE_BOTH;
		return typeForm;
	}

	private static
	TestTypeForm newMaleForm(int dim) {

		TestTypeForm typeForm = new TestTypeForm();
		typeForm.dimension = dim;
		typeForm.ident = TestTypeForm.TYPE_MALE;
		typeForm.wanted = TestTypeForm.TYPE_FEMALE;
		return typeForm;
	}


	private static @SuppressWarnings("SameParameterValue")
	TestTypeForm newFemaleForm(int dim) {

		TestTypeForm typeForm = new TestTypeForm();
		typeForm.dimension = dim;
		typeForm.ident = TestTypeForm.TYPE_FEMALE;
		typeForm.wanted = TestTypeForm.TYPE_MALE;
		return typeForm;
	}

	private static
	void monitorQueue(MatcherControllerTest context, int partyNumb, int subPartyNumb, Long ... users)
			throws Exception {

		String admin = context.getForAdminAuthToken();
		Party[] parties = context.authorizedGetRequest(MONITOR_PARTY, admin, Party[].class).getBody();
		SubParty[] subParties = context.authorizedGetRequest(MONITOR_SUBPARTY, admin, SubParty[].class).getBody();

		System.out.println("\n:::\n");
		for (Party party : parties)
			System.out.println(party);

		System.out.println("\n:::\n");
		for (SubParty subParty : subParties)
			System.out.println(subParty);

		Set<SubParty> subPartySet = new HashSet<>();
		Set<Party> partySet = new HashSet<>();

		for (Long user: users) {
			for (SubParty s : subParties) {
				if (s.getUsers().contains(user)) {
					subPartySet.add(s);
					if (s.getParty() != null) {
						partySet.add(s.getParty());
					}
				}
			}
		}

		assert subPartySet.size() == subPartyNumb;
		assert partySet.size() == partyNumb;
	}


	private static @SuppressWarnings("unchecked")
	Entry<Long, String>[] createUserTokenEntries(MatcherControllerTest context, Long[] users)
		throws Exception {

		Entry<Long, String>[] entries = new Entry[users.length];
		for (int i = 0; i < entries.length; i++)
			entries[i] = new AbstractMap.SimpleEntry<>(users[i], context.getForUserAuthToken(users[i]));
		return entries;
	}


	@Test
	public void runSoloQueueTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 60);
		Thread[] threads = new Thread[users.length];

		for (int i = 0; i < threads.length; i++) {

			final String token = getForUserAuthToken(users[i]);
			threads[i] = new Thread(() -> authorizedPostRequest(SOLO, token, newDefaultForm(3)));
		}

		for (Thread t: threads) t.start();

		Thread.sleep(2_000);

		monitorQueue(this, 10, 20, users);
	}



	@Test
	public void leaveQueueTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 1);
		String token = getForUserAuthToken(users[0]);
		authorizedPostRequest(SOLO, token, newDefaultForm(3));

		Thread.sleep(500);

		Long[] listBefore = authorizedGetRequest(LIST, token, Long[].class).getBody();
		assert listBefore.length == 1;

		authorizedDeleteRequest(LEAVE + listBefore[0], token);
		Thread.sleep(500);

		Long[] listAfter = authorizedGetRequest(LIST, token, Long[].class).getBody();
		assert listAfter.length == 0;
	}


	@Test
	public void multiLeaveQueueTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		Entry<Long, String>[] userTokenEntries = createUserTokenEntries(this, users);

		for (Entry<Long, String> entry: userTokenEntries) {
			authorizedPostRequest(SOLO, entry.getValue(), newDefaultForm(2));
		}
		Thread.sleep(500);

		Long[] list = authorizedGetRequest(LIST, userTokenEntries[1].getValue(), Long[].class).getBody();
		assert list.length == 1;

		monitorQueue(this, 1, 2, users);

		authorizedDeleteRequest(LEAVE + list[0], userTokenEntries[1].getValue());
		Thread.sleep(500);

		monitorQueue(this, 0, 2, users);
		authorizedPostRequest(SOLO, userTokenEntries[0].getValue(), newDefaultForm(2)); // never change

		Thread.sleep(500);
		monitorQueue(this, 0, 2, users);
		monitorQueue(this, 0, 0, userTokenEntries[1].getKey());
	}


	@Test
	public void customQueueStartTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 6);
		Entry<Long, String>[] userTokenEntries = createUserTokenEntries(this, users);

		Long custom1 = authorizedPostRequest(CUSTOM, userTokenEntries[0].getValue(), newFemaleForm(3), Long.class).getBody();
		Long custom2 = authorizedPostRequest(CUSTOM, userTokenEntries[3].getValue(), newMaleForm(3), Long.class).getBody();

		for (int i = 0; i < 3; i++) {
			Entry<Long, String> entry1 = userTokenEntries[i];
			Entry<Long, String> entry2 = userTokenEntries[i + 3];

			friendshipService.addFriendshipRelation(entry1.getKey(), users[0]);
			friendshipService.addFriendshipRelation(entry2.getKey(), users[3]);

			Boolean joined1 = authorizedPostRequest(JOIN + custom1, entry1.getValue(), null, Boolean.class).getBody();
			assert joined1;

			Boolean joined2 = authorizedPostRequest(JOIN + custom2, entry2.getValue(), null, Boolean.class).getBody();
			assert joined2;
		}

		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[0].getValue(), Long[].class).getBody().length == 1;
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[3].getValue(), Long[].class).getBody().length == 1;

		authorizedPostRequest(CUSTOM_START + custom1, userTokenEntries[0].getValue(), null);
		authorizedPostRequest(CUSTOM_START + custom2, userTokenEntries[3].getValue(), null);

		Thread.sleep(2_500);

		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[0].getValue(), Long[].class).getBody().length == 0;
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[3].getValue(), Long[].class).getBody().length == 0;

		monitorQueue(this, 1, 2, users);
	}


	@Test
	public void customQueueInviteAndLeaveTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 3);
		for (Long user: users) friendshipService.addFriendshipRelation(user, users[0]);
		Entry<Long, String>[] userTokenEntries = createUserTokenEntries(this, users);

		Long custom1 = authorizedPostRequest(CUSTOM, userTokenEntries[0].getValue(), newMaleForm(3), Long.class).getBody();
		for (Entry<Long, String> entry : userTokenEntries) {
			Boolean joined1 = authorizedPostRequest(JOIN + custom1, entry.getValue(), null, Boolean.class).getBody();
			assert joined1;
		}

		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[1].getValue(), Long[].class).getBody().length == 1;
		assert authorizedDeleteRequest(CUSTOM_LEAVE + custom1, userTokenEntries[1].getValue(), Boolean.class).getBody();
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[1].getValue(), Long[].class).getBody().length == 0;

		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[0].getValue(), Long[].class).getBody().length == 1;
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[2].getValue(), Long[].class).getBody().length == 1;

		assert authorizedDeleteRequest(CUSTOM_LEAVE + custom1, userTokenEntries[0].getValue(), Boolean.class).getBody();
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[0].getValue(), Long[].class).getBody().length == 0;
		assert authorizedGetRequest(CUSTOM_LIST, userTokenEntries[2].getValue(), Long[].class).getBody().length == 0;
	}


	@Test
	public void customQueueDeleteTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 4);
		for (Long user: users) friendshipService.addFriendshipRelation(user, users[0]);
		Entry<Long, String>[] userTokenEntries = createUserTokenEntries(this, users);

		Long custom1 = authorizedPostRequest(CUSTOM, userTokenEntries[0].getValue(), newMaleForm(4), Long.class).getBody();
		for (Entry<Long, String> entry : userTokenEntries) {
			Boolean joined1 = authorizedPostRequest(JOIN + custom1, entry.getValue(), null, Boolean.class).getBody();
			assert joined1;
		}

		for (Entry<Long, String> entry : userTokenEntries) {
			assert authorizedGetRequest(CUSTOM_LIST, entry.getValue(), Long[].class).getBody().length == 1;
		}

		// NOT CUSTOM QUEUE OWNER SO CANT DELETE
		assert !authorizedDeleteRequest(CUSTOM_REMOVE + custom1, userTokenEntries[1].getValue(), Boolean.class).getBody();

		// NOTHING CHANGED
		for (Entry<Long, String> entry : userTokenEntries) {
			assert authorizedGetRequest(CUSTOM_LIST, entry.getValue(), Long[].class).getBody().length == 1;
		}

		// QUEUE OWNER SO REMOVED
		assert authorizedDeleteRequest(CUSTOM_REMOVE + custom1, userTokenEntries[0].getValue(), Boolean.class).getBody();

		// NOW EMPTY
		for (Entry<Long, String> entry : userTokenEntries) {
			assert authorizedGetRequest(CUSTOM_LIST, entry.getValue(), Long[].class).getBody().length == 0;
		}
	}

}