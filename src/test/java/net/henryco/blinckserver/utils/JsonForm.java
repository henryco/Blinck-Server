package net.henryco.blinckserver.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Henry on 26/08/17.
 */
public interface JsonForm {


	final class AdminLoginPost implements JsonForm {

		public @JsonProperty String user_id;
		public @JsonProperty String password;

		public AdminLoginPost(final String user_id,
							  final String password) {
			this.user_id = user_id;
			this.password = password;
		}

		public static AdminLoginPost randomOne() {
			return new AdminLoginPost(
					TestUtils.randomGaussNumberString(), // random uid
					TestUtils.randomGaussNumberString() // random password
			);
		}
	}


	final class UserLoginPost implements JsonForm {

		public @JsonProperty String facebook_uid;
		public @JsonProperty String facebook_token;

		public UserLoginPost(final String facebook_uid,
							 final String facebook_token) {
			this.facebook_uid = facebook_uid;
			this.facebook_token = facebook_token;
		}

		public static UserLoginPost randomOne() {
			return new UserLoginPost(
					TestUtils.randomGaussNumberString(), // random uid
					TestUtils.randomGaussNumberString() // random password
			);
		}
	}

}