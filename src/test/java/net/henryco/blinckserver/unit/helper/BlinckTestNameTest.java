package net.henryco.blinckserver.unit.helper;

import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.test.BlinckTestName;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static net.henryco.blinckserver.util.test.BlinckTestUtil.getMethod;

/**
 * @author Henry on 27/08/17.
 */

public class BlinckTestNameTest extends BlinckUnitTest {

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


	@Test
	public void innerStaticMethodTest() throws Exception{

		Method getOne = getMethod(PrivateMethodsClass.class, "i1.InnerTwo.getOne");
		assert getOne.invoke(null).equals(1);

		Method getTwo = getMethod(PrivateMethodsClass.class, "i1.InnerTwo.two");
		assert getTwo.invoke(null).equals(2d);
	}


	@Test
	public void innerRegularMethodTest() throws Exception {

		Method get = getMethod(PrivateMethodsClass.class, "i1.i3.wow");

		try {
			get.invoke(null);
			assert false;
		} catch (NullPointerException e) {
			assert true;
		}

		Class<?> invoker = BlinckTestUtil.getClass(PrivateMethodsClass.class, "i1");
		Constructor<?> ctr = BlinckTestUtil.getClass(invoker, "i3").getDeclaredConstructor();
		ctr.setAccessible(true);
		assert get.invoke(ctr.newInstance()).equals("wow");
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


	@BlinckTestName("i1")
	protected static final class InnerOne {

		@BlinckTestName
		private static abstract class InnerTwo {

			@BlinckTestName
			private static int getOne() {
				return 1;
			}

			@BlinckTestName("two")
			private static double getTwo() {
				return 2d;
			}
		}

		@BlinckTestName("i3")
		private static class InnerThree {

			@BlinckTestName
			protected String wow() {
				return "wow";
			}
		}
	}

}