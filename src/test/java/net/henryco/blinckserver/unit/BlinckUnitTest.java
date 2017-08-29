package net.henryco.blinckserver.unit;

import net.henryco.blinckserver.utils.TestedLoop;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Henry on 27/08/17.
 */
@RunWith(JUnit4.class)
public abstract class BlinckUnitTest {

	protected static final TestedLoop testLoop = new TestedLoop(100);

}