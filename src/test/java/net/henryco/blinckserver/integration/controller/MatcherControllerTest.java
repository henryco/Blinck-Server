package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckStompIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.junit.Test;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Henry on 15/09/17.
 */
public class MatcherControllerTest extends BlinckStompIntegrationTest {

	private static final String LIVE_SUBPARTY_LIST = "/protected/user/subgroup/list/simple";

	private static final String MONITOR_SUBPARTY = "/protected/admin/monitor/subparty/all";
	private static final String MONITOR_PARTY = "/protected/admin/monitor/party/all";

	private static final String MATCH = "/protected/user/match";
	private static final String QUEUE = MATCH + "/queue";
	private static final String SOLO = QUEUE + "/solo";
	private static final String CUSTOM = QUEUE + "/custom";

	private static final String LIST = QUEUE + "/list";
	private static final String LEAVE = QUEUE + "/leave?id=";



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
	}


}