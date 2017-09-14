package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.service.infrastructure.MatcherService;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 13/09/17.
 */
@RestController // TODO: 15/09/17 TESTS
@RequestMapping("/protected/user/match")
public class MatcherController
		implements BlinckController, BlinckNotification {


	private final MatcherService matcherService;
	private final UpdateNotificationService notificationService;

	private final ExecutorService notificationTaskQueue;
	private final ExecutorService subPartyTaskQueue;
	private final ExecutorService partyTaskQueue;


	@Autowired
	public MatcherController(MatcherService matcherService,
							 UpdateNotificationService notificationService) {

		this.matcherService = matcherService;
		this.notificationService = notificationService;

		this.notificationTaskQueue = Executors.newFixedThreadPool(10);
		this.subPartyTaskQueue = Executors.newSingleThreadExecutor();
		this.partyTaskQueue = Executors.newSingleThreadExecutor();
	}



	/**
	 * <h1>Income Match Type JSON:</h1>
	 * <h2>
	 *     {&nbsp;
	 *         "ident":			CHAR[255], &nbsp;
	 *         "wanted":		CHAR[255], &nbsp;
	 *         "dimension":		INTEGER
	 *     &nbsp;}
	 * </h2>
	 *
	 * @see Details.Type
	 */
	public @ResponseStatus(OK) @RequestMapping(
			value = "/queue/solo",
			method = POST,
			consumes = JSON
	) void soloQueue(Authentication authentication,
					 @RequestBody Details.Type type) {

		final Long id = longID(authentication);
		final Details.Type adapted = Details.Type.typeAdapter(type);

		subPartyTaskQueue.submit(() -> {
			SubParty subParty = matcherService.jointToExistingOrCreateSubParty(id, adapted);
			if (!subParty.getDetails().getInQueue())
				partyTaskQueue.submit(() -> findParty(subParty));
		});

	}



	private void findParty(SubParty subParty) {

		Party party = matcherService.joinToExistingOrCreateParty(subParty);
		if (!party.getDetails().getInQueue())
			notificationTaskQueue.submit(() -> createPartyNotification(party));
	}

	private void createPartyNotification(Party party) {

		for (SubParty sub: party.getSubParties()) {
			for (Long userId : sub.getUsers()) {

				notificationService.addNotification(userId, TYPE.PARTY_FOUND, party.getId());
				notificationService.addNotification(userId, TYPE.SUB_PARTY_FOUND, sub.getId());
			}
		}
	}

}