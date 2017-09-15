package net.henryco.blinckserver.integration.controller;

import net.henryco.blinckserver.integration.BlinckStompIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.relation.core.PartyDao;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

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



	private static final class TestTypeForm
			implements Serializable {

		public static final String TYPE_MALE = "male";
		public static final String TYPE_FEMALE = "female";
		public static final String TYPE_BOTH = "both";

		public String ident;
		public String wanted;
		public Integer dimension;
	}

	private @Autowired PartyDao partyDao;
	private @Autowired SubPartyDao subPartyDao;

	@Before
	public final void beforeTests() {

	}


	@Test
	public void runSoloQueueTest() throws Exception {

		Long[] users = saveNewRandomUsers(this, 60);
		Thread[] threads = new Thread[users.length];

		TestTypeForm typeForm = new TestTypeForm();
		typeForm.dimension = 3;
		typeForm.ident = TestTypeForm.TYPE_BOTH;
		typeForm.wanted = TestTypeForm.TYPE_BOTH;


		for (int i = 0; i < threads.length; i++) {

			final String token = getForUserAuthToken(users[i]);
			threads[i] = new Thread(() -> authorizedPostRequest(SOLO, token, typeForm));
		}

		for (Thread t: threads) t.start();

		Thread.sleep(5_000);


		String admin = getForAdminAuthToken();
		Party[] parties = authorizedGetRequest(MONITOR_PARTY, admin, Party[].class).getBody();
		SubParty[] subParties = authorizedGetRequest(MONITOR_SUBPARTY, admin, SubParty[].class).getBody();

		System.out.println("\n:::\n");
		for (Party party : parties)
			System.out.println(party);

		System.out.println("\n:::\n");
		for (SubParty subParty : subParties)
			System.out.println(subParty);
	}




}