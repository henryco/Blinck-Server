package net.henryco.blinckserver.unit.helper;

import net.henryco.blinckserver.unit.BlinckUnitTest;
import net.henryco.blinckserver.util.test.BlinckTestUtil;
import net.henryco.blinckserver.utils.MockFacebookUser;
import org.junit.Test;
import org.springframework.social.facebook.api.User;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Henry on 29/08/17.
 */
public class MockFacebookUserTest extends BlinckUnitTest {

	private static final String FB_DATE_FORMAT = "MM/dd/yyyy";

	@Test
	public void randomIdGeneratorTest() throws Exception {

		final List<String> values = new ArrayList<>();
		testLoop.test(() -> {

			Method createNewRandomId = BlinckTestUtil.getMethod(
					MockFacebookUser.class,
					"createNewRandomId"
			);

			String id = createNewRandomId.invoke(MockFacebookUser.getInstance()).toString();
			assert !values.contains(id);
			values.add(id);
		});
	}


	@Test
	public void randomUserGeneratorIdentityTest() throws Exception {

		final List<User> userList = new ArrayList<>();
		testLoop.test(() -> {
			User user = MockFacebookUser.getInstance().createRandom().getUser();
			assert !userList.contains(user);

			for (User u: userList) {
				assert !Objects.equals(user, u);
				assert !Objects.equals(user.getId(), u.getId());
			}

			userList.add(user);
		});
	}


	@Test
	public void randomUserIsNotNullTest() throws Exception {

		testLoop.test(() -> {
			User user = MockFacebookUser.getInstance().createRandom().getUser();

			assert user != null;
			assert stringNutNullAndNotEmpty(user.getId());
			assert stringNutNullAndNotEmpty(user.getName());
			assert stringNutNullAndNotEmpty(user.getFirstName());
			assert stringNutNullAndNotEmpty(user.getLastName());
			assert stringNutNullAndNotEmpty(user.getGender());
			assert user.getLocale() != null;
		});
	}


	@Test
	public void randomBirthdayDateTest() throws Exception {

		testLoop.test(() -> {
			User user = MockFacebookUser.getInstance().createRandom().getUser();
			new SimpleDateFormat(FB_DATE_FORMAT).parse(user.getBirthday());
		});
	}


	private static boolean
	stringNutNullAndNotEmpty(String s) {
		return s != null && !s.isEmpty();
	}


}