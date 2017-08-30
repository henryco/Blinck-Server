package net.henryco.blinckserver.util.dao.repo;

import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.entity.BlinckEntityRemovalForbiddenException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author Henry on 30/08/17.
 */
public abstract class BlinckRepositoryProvider<ENTITY, KEY extends Serializable>
		implements BlinckDaoTemplate<ENTITY, KEY> {

	private final JpaRepository<ENTITY, KEY> repository;
	private final boolean removable;

	public BlinckRepositoryProvider(JpaRepository<ENTITY, KEY> repository,
									boolean removable) {
		this.repository = repository;
		this.removable = removable;
	}

	public BlinckRepositoryProvider(JpaRepository<ENTITY, KEY> repository) {
		this(repository, true);
	}

	protected JpaRepository<ENTITY, KEY> provideRepository() {
		return repository;
	}

	@Override
	public ENTITY getById(KEY id) {
		return provideRepository().getOne(id);
	}

	@Override
	public ENTITY save(ENTITY entity) {
		return provideRepository().save(entity);
	}

	@Override
	public boolean isExists(KEY id) {
		return provideRepository().exists(id);
	}

	@Override
	public void deleteById(KEY id) {
		if (!removable) throw new BlinckEntityRemovalForbiddenException();
		provideRepository().delete(id);
	}

}