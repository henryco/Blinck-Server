package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.infrastructure.MatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Henry on 13/09/17.
 */
@RestController
@RequestMapping("/protected/user/match")
public class MatcherController implements BlinckController {


	private final MatcherService matcherService;

	@Autowired
	public MatcherController(MatcherService matcherService) {
		this.matcherService = matcherService;
	}

}