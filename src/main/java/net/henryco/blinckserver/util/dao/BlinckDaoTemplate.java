package net.henryco.blinckserver.util.dao;

/**
 * @author Henry on 23/08/17.
 */
public interface BlinckDaoTemplate<ENTITY, ID_TYPE> {

	ENTITY getById(ID_TYPE id);
	ENTITY save(ENTITY entity);
	boolean isExists(ID_TYPE id);
	void deleteById(ID_TYPE id);

}
