package net.henryco.blinckserver.mvc.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

/**
 * @author Henry on 04/09/17.
 */
public interface BlinckController {

	String JSON = "application/json; charset=UTF-8";
	String ROLE_MODERATOR = "ROLE_MODERATOR";
	String ROLE_ADMIN = "ROLE_ADMIN";
	String ROLE_USER = "ROLE_USER";

	interface EndpointAPI {

		String PUBLIC = "/public";
		String SESSION = "/session";

		String ADMIN = "/protected/admin";
		String ADMIN_MONITOR = "/protected/admin/monitor";
		String ADMIN_BANS = "/protected/admin/bans";

		String USER = "/protected/user";
		String USER_NOTIFICATIONS = "/protected/user/notifications";
		String USER_MEDIA = "/protected/user/media";

		String FRIENDSHIP = "/protected/user/friends";
		String FRIENDSHIP_CONVERSATION = "/protected/user/friends/conversation";

		String MATCHER = "/protected/user/match";
		String MEETING = "/protected/user/group/meeting";

		String GROUP = "/protected/user/group";
		String GROUP_CONVERSATION = "/protected/user/group/conversation";

		String SUB_GROUP = "/protected/user/subgroup";
		String SUB_GROUP_CONVERSATION = "/protected/user/subgroup/conversation";

		String REPORT = "/protected/user/report";
	}


	default void rolesRequired(Authentication authentication,
							   String ... authorities) {
		for (String auth: authorities)
			if (authentication.getAuthorities().stream().noneMatch(sga -> sga.getAuthority().equals(auth)))
				throw new AccessDeniedException(auth+" required");
	}

	default Long longID(Authentication authentication) {
		return Long.decode(authentication.getName());
	}
}