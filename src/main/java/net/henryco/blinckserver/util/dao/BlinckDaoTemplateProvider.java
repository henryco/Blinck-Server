package net.henryco.blinckserver.util.dao;

/**
 * @author Henry on 29/08/17.
 */
public interface BlinckDaoTemplateProvider<ENTITY, ID_TYPE>
		extends BlinckDaoTemplate<ENTITY, ID_TYPE> {


	BlinckDaoTemplate<ENTITY, ID_TYPE> provideDao();


	@Override
	default ENTITY getById(ID_TYPE id) {
		return provideDao().getById(id);
	}

	@Override
	default ENTITY save(ENTITY ENTITY) {
		return provideDao().save(ENTITY);
	}

	@Override
	default boolean isExists(ID_TYPE id) {
		return provideDao().isExists(id);
	}

	@Override
	default void deleteById(ID_TYPE id) {
		provideDao().deleteById(id);
	}

}