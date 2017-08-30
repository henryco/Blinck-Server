package net.henryco.blinckserver.unit.auth.details;


import net.henryco.blinckserver.security.details.BlinckDetailsProfile;
import net.henryco.blinckserver.security.details.BlinckDetailsProfileService;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import net.henryco.blinckserver.util.entity.BlinckEntityRemovalForbiddenException;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class DaoTemplateServiceTest extends DetailsServicesTest {


	private static final String KEY_ID = testEntity.getId().toString();

	private static BlinckDetailsProfileService<Float>
	createDetailsService(BlinckDaoTemplate<TestEntity, Float> daoTemplate) {
		return new TestDetailsService(
				daoTemplate,
				Float::parseFloat,	// KEY: String -> Float
				String::valueOf		// KEY: Float -> String
		);
	}

	private static void
	assertDetails(UserDetails userDetails) {
		assert userDetails != null;
		assert userDetails.getUsername().equals(KEY_ID);
	}




	@Test
	public void authorityEntityTest() {

		final String someAuthority = "ROLE_WTF";

		TestEntity entity = new TestEntity();
		entity.setAuthorityArray(someAuthority);

		assert entity.getAuthorities().equals("["+someAuthority+"]");
		assert entity.getAuthorityArray().length == 1;
		assert entity.getAuthorityArray()[0].equals(someAuthority);
	}



	@Test
	public void detailsProfileTest() {

		TestEntity entity = DetailsServicesTest.testEntity;

		Function<Float, String>
				decoder = String::valueOf; // KEY: Float -> String

		UserDetails userDetails = new BlinckDetailsProfile<>(
				entity, decoder
		);

		String username = userDetails.getUsername();
		String decoded = decoder.apply(entity.getId());

		assert decoded.equals(username);
	}



	@Test
	public void pureDaoTemplateTest() {

		BlinckDaoTemplate<TestEntity, Float>
				pureDaoTemplate = new TestDaoTemplatePure();

		BlinckDetailsProfileService<Float> service =
				createDetailsService(pureDaoTemplate);

		UserDetailsService detailsService = service;
		UserDetails userDetails = detailsService.loadUserByUsername(KEY_ID);

		assertDetails(userDetails);
	}



	@Test
	public void daoTemplateProviderTest() {

		BlinckDaoTemplate<TestEntity, Float>
				pureDaoTemplate = new TestDaoTemplatePure();

		BlinckDaoProvider<TestEntity, Float>
				provider = new TestDaoTemplateProvided(pureDaoTemplate);

		BlinckDetailsProfileService<Float>
				service = createDetailsService(provider);

		assertDetails(service.loadUserByUsername(KEY_ID));
	}



	@Test
	public void repositoryProviderTest() {

		TestEntity entity = DetailsServicesTest.testEntity;

		JpaRepository<TestEntity, Float> jpaRepo = testJpaRepo;

		BlinckRepositoryProvider<TestEntity, Float>
				repositoryProvider = new TestRepositoryProvider(jpaRepo, false);

		assert repositoryProvider.isExists(entity.getId());
		assert repositoryProvider.getById(entity.getId()) == entity;

		try {
			repositoryProvider.deleteById(entity.getId());
			assert false;
		} catch (BlinckEntityRemovalForbiddenException e) {
			// WE CANT REMOVE BECAUSE: (REMOVABLE == FALSE)
			assert true;
		}
	}



	@Test
	public void repositoryProviderDetailsServiceTest() {

		JpaRepository<TestEntity, Float> jpaRepo = testJpaRepo;

		BlinckRepositoryProvider<TestEntity, Float>
				repositoryProvider = new TestRepositoryProvider(jpaRepo, false);

		BlinckDetailsProfileService<Float>
				service = createDetailsService(repositoryProvider);

		assertDetails(service.loadUserByUsername(KEY_ID));
	}



}