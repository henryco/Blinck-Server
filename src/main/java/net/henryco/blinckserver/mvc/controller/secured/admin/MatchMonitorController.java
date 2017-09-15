package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 15/09/17.
 */
@RestController
@RequestMapping("/protected/admin/monitor")
public class MatchMonitorController implements BlinckController {


	private final PartyService partyService;
	private final SubPartyService subPartyService;


	@Autowired
	public MatchMonitorController(PartyService partyService,
								  SubPartyService subPartyService) {
		this.partyService = partyService;
		this.subPartyService = subPartyService;
	}


	public @RequestMapping(
			value = "/party/all",
			method = GET,
			produces = JSON
	) Party[] getAllParties() {
		return partyService.getAllParties();
	}

	public @RequestMapping(
			value = "/subparty/all",
			method = GET,
			produces = JSON
	) SubParty[] getAllSubParties() {
		return subPartyService.getAllSubParties();
	}

}