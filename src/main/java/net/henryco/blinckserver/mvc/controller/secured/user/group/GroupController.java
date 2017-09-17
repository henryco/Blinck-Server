package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
final class GroupServicePack {

	protected final PartyService partyService;

	@Autowired
	public GroupServicePack(PartyService partyService) {
		this.partyService = partyService;
	}
}

@RestController // TODO: 17/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.GROUP)
public class GroupController implements BlinckController {

	private final GroupServicePack servicePack;

	@Autowired
	public GroupController(GroupServicePack servicePack) {
		this.servicePack = servicePack;
	}



}