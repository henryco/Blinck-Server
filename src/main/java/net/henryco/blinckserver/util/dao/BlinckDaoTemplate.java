package net.henryco.blinckserver.util.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Henry on 23/08/17.
 */
public interface BlinckDaoTemplate<ENTITY, ID_TYPE> {

	@Transactional ENTITY getById(ID_TYPE id);
	@Transactional ENTITY save(ENTITY entity);
	@Transactional boolean isExists(ID_TYPE id);
	@Transactional void deleteById(ID_TYPE id);
	@Transactional List<ENTITY> getFirst(int n);
	@Transactional List<ENTITY> getAll();
	@Transactional long count();

}
