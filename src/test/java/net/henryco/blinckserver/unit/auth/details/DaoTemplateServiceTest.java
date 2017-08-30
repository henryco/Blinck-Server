package net.henryco.blinckserver.unit.auth.details;


import net.henryco.blinckserver.security.details.BlinckDetailsProfileService;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Henry on 30/08/17.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class DaoTemplateServiceTest extends DetailsServicesTest {



	@Test
	public void pureDaoTemplateTest() {

		final String id = testEntity.getId().toString();

		BlinckDaoTemplate<TestEntity, Float>
				pureDaoTemplate = new TestDaoTemplatePure();

		BlinckDetailsProfileService<Float> service = new TestDetailsService(
				pureDaoTemplate,
				Float::parseFloat,	// KEY: String -> Float
				String::valueOf		// KEY: Float -> String
		);

		UserDetailsService detailsService = service;
		UserDetails userDetails = detailsService.loadUserByUsername(id);

		assert userDetails != null;
		assert userDetails.getUsername().equals(id);
	}





}