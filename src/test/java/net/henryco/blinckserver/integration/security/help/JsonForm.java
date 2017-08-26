package net.henryco.blinckserver.integration.security.help;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.henryco.blinckserver.utils.HTTPTestUtils;

/**
 * @author Henry on 26/08/17.
 */
public interface JsonForm {


	final class AdminLoginPost implements JsonForm {

		@JsonProperty String user_id;
		@JsonProperty String password;

		public AdminLoginPost(final String user_id,
							  final String password) {
			this.user_id = user_id;
			this.password = password;
		}

		public static AdminLoginPost randomOne() {
			return new AdminLoginPost(
					HTTPTestUtils.randomNumberString(), // random uid
					HTTPTestUtils.randomNumberString() // random password
			);
		}
	}


	final class UserLoginPost implements JsonForm {

		@JsonProperty String facebook_uid;
		@JsonProperty String facebook_token;

		public UserLoginPost(final String facebook_uid,
							 final String facebook_token) {
			this.facebook_uid = facebook_uid;
			this.facebook_token = facebook_token;
		}

		public static UserLoginPost randomOne() {
			return new UserLoginPost(
					HTTPTestUtils.randomNumberString(), // random uid
					HTTPTestUtils.randomNumberString() // random password
			);
		}
	}

}