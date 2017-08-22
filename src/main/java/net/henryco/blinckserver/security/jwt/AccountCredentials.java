package net.henryco.blinckserver.security.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Henry on 22/08/17.
 */ @Data @NoArgsConstructor
public class AccountCredentials {

	private String username;
	private String password;

}
