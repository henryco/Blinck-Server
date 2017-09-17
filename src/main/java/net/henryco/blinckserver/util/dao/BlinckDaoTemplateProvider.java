package net.henryco.blinckserver.util.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 29/08/17.
 */
public abstract class BlinckDaoTemplateProvider<ENTITY, ID_TYPE>
		implements BlinckDaoTemplate<ENTITY, ID_TYPE> {


	protected abstract <T extends BlinckDaoTemplate<ENTITY, ID_TYPE>> T provideDao();


	@Override @Transactional
	public ENTITY getById(ID_TYPE id) {
		return provideDao().getById(id);
	}

	@Override @Transactional
	public ENTITY save(ENTITY ENTITY) {
		return provideDao().save(ENTITY);
	}

	@Override @Transactional
	public boolean isExists(ID_TYPE id) {
		return provideDao().isExists(id);
	}

	@Override @Transactional
	public void deleteById(ID_TYPE id) {
		provideDao().deleteById(id);
	}

	@Override @Transactional
	public List<ENTITY> getFirst(int n) {
		return provideDao().getFirst(n);
	}

	@Override @Transactional
	public List<ENTITY> getAll() {
		return provideDao().getAll();
	}

	@Override @Transactional
	public long count() {
		return provideDao().count();
	}
}