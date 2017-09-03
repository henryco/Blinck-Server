package net.henryco.blinckserver.util.dao.repo;

import net.henryco.blinckserver.util.dao.BlinckDaoTemplate;
import net.henryco.blinckserver.util.entity.BlinckEntityRemovalForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

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

	@SuppressWarnings("unchecked")
	protected <T extends JpaRepository<ENTITY, KEY>> T provideRepository() {
		return (T) repository;
	}

	@Override @Transactional
	public ENTITY getById(KEY id) {
		return provideRepository().getOne(id);
	}

	@Override @Transactional
	public ENTITY save(ENTITY entity) {
		return provideRepository().saveAndFlush(entity);
	}

	@Override @Transactional
	public boolean isExists(KEY id) {
		return provideRepository().exists(id);
	}

	@Override @Transactional
	public void deleteById(KEY id) {
		if (!removable) throw new BlinckEntityRemovalForbiddenException();
		provideRepository().delete(id);
	}

	@Override
	public List<ENTITY> getFirst(int n) {
		return provideRepository().findAll(new PageRequest(0, n)).getContent();
	}

	@Override @Transactional
	public List<ENTITY> getAll() {
		return provideRepository().findAll();
	}

	@Override
	public long count() {
		return provideRepository().count();
	}
}