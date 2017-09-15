package net.henryco.blinckserver.integration.entity;

import net.henryco.blinckserver.integration.BlinckUserIntegrationTest;
import net.henryco.blinckserver.mvc.model.dao.relation.core.SubPartyDao;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.mvc.model.repository.relation.core.SubPartyRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type.FEMALE;
import static net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type.MALE;


/**
 * @author Henry on 12/09/17.
 */
public class SubPartyEntityTest extends BlinckUserIntegrationTest {


	private @Autowired SubPartyRepository subPartyRepository;
	private @Autowired SubPartyDao subPartyDao;

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
		Details details1 = new Details();
		details1.setType(Type.newFemFem(3));
		details1.setInQueue(true);
		subParty1.setDetails(details1);
		subParty1.setUsers(users1);

		SubParty subParty2 = new SubParty();
		Details details2 = new Details();
		details2.setType(Type.newFemFem(3));
		details2.setInQueue(true);
		subParty2.setDetails(details2);
		subParty2.setUsers(users2);

		SubParty subParty3 = new SubParty();
		Details details3 = new Details();
		details3.setType(Type.newMaleFem(3));
		details3.setInQueue(true);
		subParty3.setDetails(details3);
		subParty3.setUsers(users3);

		SubParty subParty4 = new SubParty();
		Details details4 = new Details();
		details4.setType(Type.newFemMale(3));
		details4.setInQueue(true);
		subParty4.setDetails(details4);
		subParty4.setUsers(users4);

		SubParty subParty5 = new SubParty();
		Details details5 = new Details();
		details5.setType(Type.newFemMale(3));
		details5.setInQueue(true);
		subParty5.setDetails(details5);
		subParty5.setUsers(users5);

		subPartyRepository.saveAndFlush(subParty1);
		subPartyRepository.saveAndFlush(subParty2);
		subPartyRepository.saveAndFlush(subParty3);
		subPartyRepository.saveAndFlush(subParty4);
		subPartyRepository.saveAndFlush(subParty5);

		List<SubParty> all1 = subPartyRepository.getAllByUsersIsContainingAndDetails_InQueueIsTrue(user1);
		List<SubParty> all2 = subPartyRepository.getAllByUsersIsContainingAndDetails_InQueueIsTrue(user2);
		List<SubParty> all3 = subPartyRepository.getAllByUsersIsContainingAndDetails_InQueueIsTrue(user3);
		List<SubParty> all4 = subPartyRepository.getAllByUsersIsContainingAndDetails_InQueueIsTrue(user4);
		List<SubParty> all5 = subPartyRepository.getAllByUsersIsContainingAndDetails_InQueueIsTrue(user5);

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
		Details details1 = new Details();
		details1.setType(Type.newFemFem(3));
		details1.setInQueue(true);
		subParty1.setDetails(details1);
		subParty1.setUsers(users1);

		SubParty subParty2 = new SubParty();
		Details details2 = new Details();
		details2.setType(Type.newFemFem(3));
		details2.setInQueue(true);
		subParty2.setDetails(details2);
		subParty2.setUsers(users2);

		SubParty subParty3 = new SubParty();
		Details details3 = new Details();
		details3.setType(Type.newMaleFem(3));
		details3.setInQueue(true);
		subParty3.setDetails(details3);
		subParty3.setUsers(users3);

		SubParty subParty4 = new SubParty();
		Details details4 = new Details();
		details4.setType(Type.newFemMale(3));
		details4.setInQueue(true);
		subParty4.setDetails(details4);
		subParty4.setUsers(users4);

		subPartyRepository.saveAndFlush(subParty1);
		subPartyRepository.saveAndFlush(subParty2);
		subPartyRepository.saveAndFlush(subParty3);
		subPartyRepository.saveAndFlush(subParty4);

		List<SubParty> all1 = subPartyRepository.getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
				Type.newFemFem(3).getWanted(),
				Type.newFemFem(3).getIdent(),
				3,
				true
		);

		List<SubParty> all2 = subPartyRepository.getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
				Type.newFemMale(3).getWanted(),
				Type.newFemMale(3).getIdent(),
				3,
				true
		);

		assert all1.size() == 2;
		all1.forEach(subParty -> {
			assert subParty.getDetails().getType().getIdent().equals(FEMALE);
			assert subParty.getDetails().getType().getWanted().equals(FEMALE);
			assert subParty.getDetails().getType().getDimension().equals(3);
			assert subParty.getDetails().getInQueue();
		});

		assert all2.size() == 1;
		all2.forEach(subParty -> {
			assert subParty.getDetails().getType().getIdent().equals(FEMALE);
			assert subParty.getDetails().getType().getWanted().equals(MALE);
			assert subParty.getDetails().getInQueue();
		});
	}


	@Test
	public void randomFirstInQueueTest() throws Exception {

		for (int i = 0; i < 60; i++) {



		}
	}



}