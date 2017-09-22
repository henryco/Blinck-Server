package net.henryco.blinckserver.unit.helper;

import net.henryco.blinckserver.mvc.service.profile.UserImageMediaService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author Henry on 22/09/17.
 */
public class MediaImageArrayHelper extends BlinckUnitTest {



	private static String[] delete(int index, final String ... array) throws Exception {

		Method method = BlinckTestUtil.getMethod(UserImageMediaService.class, "ArrayHelper.delete");
		return (String[]) method.invoke(null, index, array);
	}

	private static String[] put(int index, String element, final String ... array) throws Exception {

		Method method = BlinckTestUtil.getMethod(UserImageMediaService.class, "ArrayHelper.put");
		return (String[]) method.invoke(null, index, element, array);
	}


	private static String[] swap(int from, int to, final String ... array) throws Exception {

		Method method = BlinckTestUtil.getMethod(UserImageMediaService.class, "ArrayHelper.swap");
		return (String[]) method.invoke(null, from, to, array);
	}

	private static String[] add(String element, final String ... array) throws Exception {

		Method method = BlinckTestUtil.getMethod(UserImageMediaService.class, "ArrayHelper.add");
		return (String[]) method.invoke(null, element, array);
	}


	private static void assertion(String[] array, String ... vararg) {
		Assert.assertArrayEquals(vararg, array);
	}



	@Test
	public void testAdd() throws Exception {

		String[] one = add("wow", "1", "2", "3", "4", "what", "5");
		assertion(one, "1", "2", "3", "4", "what", "5", "wow");

		String[] two = add("abc", "o");
		assertion(two, "o", "abc");

		String[] three = add("what");
		assertion(three, "what");
	}

	@Test
	public void testPut() throws Exception {

		String[] one = put(0, "o", "1", "2", "3");
		assertion(one, "o", "1", "2", "3");

		String[] two = put(1, "some", "a", "3");
		assertion(two, "a", "some", "3");

		String[] three = put(3, "friend", "hello", "my");
		assertion(three, "hello", "my", "friend");

		String[] four = put(0, "nope");
		assertion(four, "nope");

		String[] five = put(2, "bai");
		assertion(five, "bai");

		String[] six = put(-3, "no", "way");
		assertion(six, "way");
	}

	@Test
	public void testDelete() throws Exception {

		String[] one = delete(3, "1", "2", "3", "4", "5");
		assertion(one, "1", "2", "3", "5");

		String[] two = delete(2, "1", "2", "3");
		assertion(two, "1", "2");

		String[] three = delete(4, "1", "2");
		assertion(three, "1", "2");

		String[] four = delete(-3, "1", "2", "3", "4");
		assertion(four, "1", "2", "3", "4");

		String[] five = delete(1);
		assertion(five);
	}

	@Test
	public void testSwap() throws Exception {

		String[] one = swap(1, 3, "1", "2", "3", "4", "5");
		assertion(one, "1", "4", "3", "2", "5");

		String[] two = swap(0, 5, "1", "2", "3", "4");
		assertion(two, "1", "2", "3", "4");

		String[] three = swap(-2, 2, "1", "2", "3");
		assertion(three, "1", "2", "3");

		String[] four = swap(1, 1, "1", "2");
		assertion(four, "1", "2");

		String[] five = swap(0, 1);
		assertion(five);
	}

}