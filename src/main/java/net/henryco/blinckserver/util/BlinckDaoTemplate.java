package net.henryco.blinckserver.util;

/**
 * @author Henry on 23/08/17.
 */
public interface BlinckDaoTemplate<T, ID> {

	T getById(ID id);
	void save(T entity);
	void deleteById(ID id);

}
