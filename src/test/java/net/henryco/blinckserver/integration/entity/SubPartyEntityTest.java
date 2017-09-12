package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Henry on 12/09/17.
 */
public class SubPartyEntityTest extends BlinckUserIntegrationTest {


	private @Autowired SubPartyRepository subPartyRepository;


	@Test @Transactional
	public void userListContainsTest() {

		Long user1 = 0L;
		Long user2 = 123L;
		Long user3 = 3453L;
		Long user4 = 1231L;
		Long user5 = 9865L;
		Long user6 = 1212121L;

		List<Long> users1 = new ArrayList<>(asList(user1, user2));
		List<Long> users2 = new ArrayList<>(asList(user1, user2, user3));
		List<Long> users3 = new ArrayList<>(asList(user3, user4, user5));
		List<Long> users4 = new ArrayList<>(asList(user1, user5, user6));
		List<Long> users5 = new ArrayList<>(asList(user1, user3, user6));

		SubParty subParty1 = new SubParty();
		subParty1.setType("someTypo1");
		subParty1.setUsers(users1);

		SubParty subParty2 = new SubParty();
		subParty2.setType("someTypo2");
		subParty2.setUsers(users2);

		SubParty subParty3 = new SubParty();
		subParty3.setType("someTypo3");
		subParty3.setUsers(users3);

		SubParty subParty4 = new SubParty();
		subParty4.setType("someTypo4");
		subParty4.setUsers(users4);

		SubParty subParty5 = new SubParty();
		subParty5.setType("someTypo5");
		subParty5.setUsers(users5);

		subPartyRepository.saveAndFlush(subParty1);
		subPartyRepository.saveAndFlush(subParty2);
		subPartyRepository.saveAndFlush(subParty3);
		subPartyRepository.saveAndFlush(subParty4);
		subPartyRepository.saveAndFlush(subParty5);

		List<SubParty> all1 = subPartyRepository.getAllByUsersIsContaining(user1);
		List<SubParty> all2 = subPartyRepository.getAllByUsersIsContaining(user2);
		List<SubParty> all3 = subPartyRepository.getAllByUsersIsContaining(user3);
		List<SubParty> all4 = subPartyRepository.getAllByUsersIsContaining(user4);
		List<SubParty> all5 = subPartyRepository.getAllByUsersIsContaining(user5);

		assert all1.contains(subParty1);
		assert all1.contains(subParty2);
		assert !all1.contains(subParty3);
		assert all1.contains(subParty4);
		assert all1.contains(subParty5);

		assert all2.contains(subParty1);
		assert all2.contains(subParty2);

		assert all3.contains(subParty2);
		assert all3.contains(subParty3);
		assert all3.contains(subParty5);

		assert all4.contains(subParty3);

		assert all5.contains(subParty3);
		assert all5.contains(subParty4);


	}


}