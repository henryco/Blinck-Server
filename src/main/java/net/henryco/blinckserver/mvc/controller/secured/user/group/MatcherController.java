package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.service.infrastructure.MatcherService;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 13/09/17.
 */
@RestController
@RequestMapping("/protected/user/match")
public class MatcherController
		implements BlinckController, BlinckNotification {


	private final MatcherService matcherService;
	private final UpdateNotificationService notificationService;


	@Autowired
	public MatcherController(MatcherService matcherService,
							 UpdateNotificationService notificationService) {
		this.matcherService = matcherService;
		this.notificationService = notificationService;
	}



	/**
	 * <h1>Income Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "ident":		CHAR[255], &nbsp;
	 *         "wanted:		CHAR[255]
	 *     &nbsp;}
	 * </h2>
	 *
	 * @see SubParty.Type
	 */
	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/solo",
			method = POST,
			consumes = JSON
	) void soloQueue(Authentication authentication,
					 @RequestBody SubParty.Type type) {

		final Long id = longID(authentication);
		final SubParty.Type adapted = SubParty.Type.typeAdapter(type);

		// TODO: 13/09/17

	}


}