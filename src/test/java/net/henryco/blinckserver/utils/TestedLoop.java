package net.henryco.blinckserver.utils;

/**
 * @author Henry on 27/08/17.
 */
public final class TestedLoop {

	private final int times;
	public TestedLoop(int times) {
		this.times = times;
	}

	@FunctionalInterface
	public interface TestedRunnable {
		void run() throws Exception;
	}

	public void test(TestedRunnable testedRunnable) throws Exception {
		for (int i = 0; i < times; i++) testedRunnable.run();
	}

}
