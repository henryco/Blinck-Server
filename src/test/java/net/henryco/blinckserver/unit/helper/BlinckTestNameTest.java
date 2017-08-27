package net.henryco.blinckserver.unit.helper;

import net.henryco.blinckserver.util.test.BlinckTestName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

import static net.henryco.blinckserver.util.test.BlinckTestUtil.getMethod;

/**
 * @author Henry on 27/08/17.
 */

@RunWith(JUnit4.class)
public class BlinckTestNameTest {


	@Test
	public void methodOneTest() throws Exception {
		Method one = getMethod(PrivateMethodsClass.class, "one");
		assert one.invoke(new PrivateMethodsClass()).toString().equals("one");
	}


	@Test
	public void methodSumTest() throws Exception {
		Method sum = getMethod(PrivateMethodsClass.class, "sum");
		assert ((int) sum.invoke(new PrivateMethodsClass(), 30, 5)) == 35;
	}


	@Test
	public void concatMethodTest() throws Exception {
		Method concat = getMethod(PrivateMethodsClass.class, "concatenation");
		assert concat.invoke(new PrivateMethodsClass(), "doge").toString().equals("doge_wow");
	}

	@Test
	public void unMarkedMethodTest() throws Exception {
		assert getMethod(PrivateMethodsClass.class, "unMarked") == null;
	}


	@Test
	public void wrongNameAnnotated() throws Exception {
		assert getMethod(PrivateMethodsClass.class, "conAteNation") == null;
	}


	@Test
	public void trueNameOverridesTest() throws Exception {

		Method methodOne = getMethod(PrivateMethodsClass.class, "methodOne");
		Method otherMethod = getMethod(PrivateMethodsClass.class, "otherMethod");

		assert methodOne.invoke(new PrivateMethodsClass()).toString().equals("one");
		assert otherMethod.invoke(new PrivateMethodsClass(), "extra").toString().equals("extra_wow");
	}

}


@SuppressWarnings("ALL")
final class PrivateMethodsClass {

	@BlinckTestName("one")
	private String methodOne() {
		return "one";
	}

	@BlinckTestName
	private int sum(int one, int two) {
		return one + two;
	}

	@BlinckTestName("concatenation")
	private String otherMethod(String text) {
		return text.concat("_wow");
	}

	private Long unMarked() {
		return 42L;
	}

}