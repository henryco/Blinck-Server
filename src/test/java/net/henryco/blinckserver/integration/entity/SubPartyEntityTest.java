package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty.Type.FEMALE;
import static net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty.Type.MALE;

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
		subParty1.setType(SubParty.Type.newFemFem());
		subParty1.setUsers(users1);

		SubParty subParty2 = new SubParty();
		subParty2.setType(SubParty.Type.newFemFem());
		subParty2.setUsers(users2);

		SubParty subParty3 = new SubParty();
		subParty3.setType(SubParty.Type.newFemFem());
		subParty3.setUsers(users3);

		SubParty subParty4 = new SubParty();
		subParty4.setType(SubParty.Type.newFemFem());
		subParty4.setUsers(users4);

		SubParty subParty5 = new SubParty();
		subParty5.setType(SubParty.Type.newFemFem());
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



	@Test @Transactional
	public void findByTypeTest() {

		List<Long> users1 = new LinkedList<>(asList(123L, 124L));
		List<Long> users2 = new LinkedList<>(asList(33L, 12L));
		List<Long> users3 = new LinkedList<>(asList(333L, 121L));
		List<Long> users4 = new LinkedList<>(asList(3333L, 1261L));

		SubParty subParty1 = new SubParty();
		subParty1.setType(SubParty.Type.newFemFem());
		subParty1.setUsers(users1);

		SubParty subParty2 = new SubParty();
		subParty2.setType(SubParty.Type.newFemFem());
		subParty2.setUsers(users2);

		SubParty subParty3 = new SubParty();
		subParty3.setType(SubParty.Type.newMaleFem());
		subParty3.setUsers(users3);

		SubParty subParty4 = new SubParty();
		subParty4.setType(SubParty.Type.newFemMale());
		subParty4.setUsers(users4);

		subPartyRepository.saveAndFlush(subParty1);
		subPartyRepository.saveAndFlush(subParty2);
		subPartyRepository.saveAndFlush(subParty3);
		subPartyRepository.saveAndFlush(subParty4);

		List<SubParty> all1 = subPartyRepository.getAllByType_WantedAndType_Ident(
				SubParty.Type.newFemFem().getWanted(),
				SubParty.Type.newFemFem().getIdent()
		);

		List<SubParty> all2 = subPartyRepository.getAllByType_WantedAndType_Ident(
				SubParty.Type.newFemMale().getWanted(),
				SubParty.Type.newFemMale().getIdent()
		);

		assert all1.size() == 2;
		all1.forEach(subParty -> {
			assert subParty.getType().getIdent().equals(FEMALE);
			assert subParty.getType().getWanted().equals(FEMALE);
		});

		assert all2.size() == 1;
		all2.forEach(subParty -> {
			assert subParty.getType().getIdent().equals(FEMALE);
			assert subParty.getType().getWanted().equals(MALE);
		});
	}


}