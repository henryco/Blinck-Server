package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Henry on 12/09/17.
 */
@RestController
@RequestMapping(BlinckController.EndpointAPI.GROUP)
public class GroupController implements BlinckController {


	private final PartyService partyService;

	@Autowired
	public GroupController(PartyService partyService) {
		this.partyService = partyService;
	}



}