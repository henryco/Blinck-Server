package net.henryco.blinckserver.unit.auth.details;

import net.henryco.blinckserver.security.details.BlinckDetailsProfileService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.dao.BlinckDaoProvider;
import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Henry on 30/08/17.
 */
public abstract class DetailsServicesTest extends BlinckUnitTest {



	protected interface
	JpaRepositoryUsedMethods {
		String GET_ONE = "getOne";
		String SAVE = "save";
		String EXISTS = "exists";
		String DELETE = "delete";
	}



	protected static final class
	TestDetailsService extends
			BlinckDetailsProfileService<Float> {

		public
		TestDetailsService(BlinckDaoTemplate<? extends BlinckAuthorityEntity<Float>, Float> authProfileDao,
						   Function<String, Float> keyConverter,
						   Function<Float, String> keyDeConverter) {
			super(authProfileDao, keyConverter, keyDeConverter);
		}
	}



	protected static final class
	TestDaoTemplatePure implements
			BlinckDaoTemplate<TestEntity, Float> {

		public @Override TestEntity
		getById(Float id) {
			return testRepo.get(id);
		}

		public @Override TestEntity
		save(TestEntity testEntity) {
			return testRepo.put(testEntity.getId(), testEntity);
		}

		public @Override boolean
		isExists(Float id) {
			return testRepo.containsKey(id);
		}

		public @Override void
		deleteById(Float id) {
			testRepo.remove(id);
		}

		@Override
		public List<TestEntity> getFirst(int n) {
			return getAll();
		}

		@Override
		public List<TestEntity> getAll() {
			return Collections.singletonList(testEntity);
		}

		@Override
		public long count() {
			return 1;
		}
	}



	protected static final class
	TestDaoTemplateProvided extends
			BlinckDaoProvider<TestEntity, Float> {

		public
		TestDaoTemplateProvided(BlinckDaoTemplate<TestEntity, Float> daoTemplate) {
			super(daoTemplate);
		}
	}



	protected static final class
	TestRepositoryProvider extends
			BlinckRepositoryProvider<TestEntity, Float> {

		public
		TestRepositoryProvider(JpaRepository<TestEntity, Float> repository,
							   final boolean removable) {
			super(repository, removable);
		}
	}



	protected static final class
	TestEntity implements
			BlinckAuthorityEntity<Float> {

		private String
				authorities = "[ROLE_TEST]";

		public @Override Float
		getId() {
			return 42F;
		}

		public @Override boolean
		isLocked() {
			return true;
		}

		public @Override boolean
		isExpired() {
			return false;
		}

		public @Override boolean
		isEnabled() {
			return true;
		}

		public @Override String
		getPassword() {
			return "test_password";
		}

		public @Override String
		getAuthorities() {
			return authorities;
		}

		public @Override void
		setAuthorities(String authorities) {
			this.authorities = authorities;
		}
	}



	protected static final
	TestEntity testEntity = new TestEntity();



	protected static final
	Map<Float, TestEntity> testRepo = Collections.singletonMap(
			testEntity.getId(), testEntity
	);



	protected static final @SuppressWarnings("unchecked")
	JpaRepository<TestEntity, Float> testJpaRepo = (JpaRepository<TestEntity, Float>) Proxy.newProxyInstance(
			ClassLoader.getSystemClassLoader(), new Class[]{JpaRepository.class}, (Object proxy, Method method, Object[] args) -> {

				switch (method.getName()) {

					case JpaRepositoryUsedMethods.GET_ONE:
						Float id = (Float) args[0];
						return testRepo.getOrDefault(id, null);

					case JpaRepositoryUsedMethods.SAVE:
						TestEntity entity = (TestEntity) args[0];
						testRepo.put(entity.getId(), entity);
						return entity;

					case JpaRepositoryUsedMethods.EXISTS:
						id = (Float) args[0];
						return testRepo.containsKey(id);

					case JpaRepositoryUsedMethods.DELETE:
						id = (Float) args[0];
						testRepo.remove(id);
						break;
				}
				return null;
			}
	);



	@Test
	public void repositoryJpaInstanceTest() {
		assert testJpaRepo.exists(testEntity.getId());
		assert testJpaRepo.getOne(testEntity.getId()) == testEntity;
	}



}