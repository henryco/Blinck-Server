package net.henryco.blinckserver.util.entity;

/**
 * @author Henry on 29/08/17.
 */
public class BlinckEntityRemovalForbiddenException extends RuntimeException {


	public BlinckEntityRemovalForbiddenException() {
		this("");
	}

	public BlinckEntityRemovalForbiddenException(String entity) {
		super("Entity " + entity + " cannot be removed in this way.");
	}

	public BlinckEntityRemovalForbiddenException(Class entity) {
		this(entity.getSimpleName());
	}
}