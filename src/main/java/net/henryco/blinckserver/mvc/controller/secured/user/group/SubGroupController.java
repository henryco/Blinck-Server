package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 12/09/17.
 */
@RestController
@RequestMapping("/protected/user/subgroup")
public class SubGroupController implements BlinckController {


	private final SubPartyService subPartyService;

	@Autowired
	public SubGroupController(SubPartyService subPartyService) {
		this.subPartyService = subPartyService;
	}


	public @RequestMapping(
			value = "/list/simple",
			method = GET,
			produces = JSON
	) Long[] getSubPartiesList(Authentication authentication) {

		final Long id = longID(authentication);
		return Arrays.stream(subPartyService.getAllSubPartiesWithUserInParty(id))
				.map(SubParty::getId)
		.toArray(Long[]::new);
	}

}