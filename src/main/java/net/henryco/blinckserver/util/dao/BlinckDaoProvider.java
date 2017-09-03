package net.henryco.blinckserver.util.dao;

/**
 * @author Henry on 29/08/17.
 */
public abstract class BlinckDaoProvider<ENTITY, ID_TYPE>
		extends BlinckDaoTemplateProvider<ENTITY, ID_TYPE> {

	private final BlinckDaoTemplate<ENTITY, ID_TYPE> daoTemplate;

	public BlinckDaoProvider(BlinckDaoTemplate<ENTITY, ID_TYPE> daoTemplate) {
		this.daoTemplate = daoTemplate;
	}


	@Override @SuppressWarnings("unchecked")
	protected <T extends BlinckDaoTemplate<ENTITY, ID_TYPE>> T provideDao() {
		return (T) daoTemplate;
	}


}