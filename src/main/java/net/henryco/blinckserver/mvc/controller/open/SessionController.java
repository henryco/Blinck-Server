package net.henryco.blinckserver.mvc.controller.open;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.security.token.service.SessionWhiteListService;
import net.henryco.blinckserver.security.token.service.TokenAuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 03/09/17.
 */ @RestController
@RequestMapping(BlinckController.EndpointAPI.SESSION)
public class SessionController {

	private static final String DEFAULT_PRINCIPAL = "unknown";

	private static final String LOGOUT_OK = "OK";
	private static final String LOGOUT_FAIL = "FAIL";

	private final TokenAuthenticationService userTokenService;
	private final TokenAuthenticationService adminTokenService;
	private final SessionWhiteListService whiteListService;

	@Autowired
	public SessionController(@Qualifier("UserTokenAuthService") TokenAuthenticationService userTokenService,
							 @Qualifier("AdminTokenAuthService") TokenAuthenticationService adminTokenService,
							 SessionWhiteListService whiteListService) {
		this.userTokenService = userTokenService;
		this.adminTokenService = adminTokenService;
		this.whiteListService = whiteListService;
	}


	/**
	 * 	<h1>Status Response JSON:</h1>
	 * 	<h2>
	 * 	    {&nbsp;
	 * 	        "principal":	CHAR[255], &nbsp;
	 * 	        "active":		BOOLEAN
	 * 	    &nbsp;}
	 * 	</h2>
	 */ @Data @NoArgsConstructor @AllArgsConstructor
	private static final class StatusResponse
			implements Serializable {
		private String principal = DEFAULT_PRINCIPAL;
		private Boolean active = false;
	}



	public @RequestMapping(
			method = GET,
			value = "/user"
	) StatusResponse getUserTokenStatus(HttpServletRequest request) {

		Authentication user = userTokenService.getAuthentication(request);
		if (user == null) return new StatusResponse();

		return new StatusResponse(user.getName(),
				whiteListService.isUserInTheWhiteList(
						Long.decode(user.getName()))
		);
	}



	public @RequestMapping(
			method = GET,
			value = "/admin"
	) StatusResponse getAdminTokenStatus(HttpServletRequest request) {

		Authentication admin = adminTokenService.getAuthentication(request);
		if (admin == null) return new StatusResponse();

		return new StatusResponse(admin.getName(),
				whiteListService.isAdminInTheWhiteList(admin.getName())
		);
	}



	public @RequestMapping(
			method = {GET, POST},
			value = "/user/logout"
	) String logoutUser(HttpServletRequest request) {
		Authentication user = userTokenService.getAuthentication(request);
		if (user == null) return LOGOUT_FAIL;
		whiteListService.removeUserFromWhiteList(Long.decode(user.getName()));
		return LOGOUT_OK;
	}



	public @RequestMapping(
			method = {GET, POST},
			value = "/admin/logout"
	) String logoutAdmin(HttpServletRequest request) {
		Authentication admin = adminTokenService.getAuthentication(request);
		if (admin == null) return LOGOUT_FAIL;
		whiteListService.removeAdminFromWhiteList(admin.getName());
		return LOGOUT_OK;
	}


}