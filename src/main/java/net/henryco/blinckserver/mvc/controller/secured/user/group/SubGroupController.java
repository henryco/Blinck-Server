package net.henryco.blinckserver.mvc.controller.secured.user.group;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.mvc.service.infrastructure.UpdateNotificationService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static net.henryco.blinckserver.mvc.service.relation.core.SubPartyService.SubPartyInfo;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Component
final class SubGroupServicePack {

	protected final SubPartyService subPartyService;
	protected final UpdateNotificationService notificationService;

	@Autowired
	public SubGroupServicePack(SubPartyService subPartyService,
							   UpdateNotificationService notificationService) {
		this.subPartyService = subPartyService;
		this.notificationService = notificationService;
	}
}


/**
 * @author Henry on 12/09/17.
 */
@RestController
@RequestMapping(BlinckController.EndpointAPI.SUB_GROUP)
public class SubGroupController implements BlinckController {


	private final SubGroupServicePack servicePack;

	@Autowired
	public SubGroupController(SubGroupServicePack servicePack) {
		this.servicePack = servicePack;
	}


	public @RequestMapping(
			value = "/list/id",
			method = GET,
			produces = JSON
	) Long[] getSubPartiesList(Authentication authentication) {
		return servicePack.subPartyService.getSubPartiesIdListWithUserInParty(longID(authentication));
	}


	public @RequestMapping(
			value = "/list/details",
			method = GET,
			produces = JSON
	) SubPartyInfo[] getSubPartiesDetailedList(Authentication authentication) {
		return servicePack.subPartyService.getSubPartyInfoListWithUserInParty(longID(authentication));
	}


	public @RequestMapping(
			value = "/details",
			method = GET,
			produces = JSON
	) SubPartyInfo getSubParty(Authentication authentication,
											 @RequestParam("id") Long subPartyId) {
		return servicePack.subPartyService.getSubPartyInfoByIdAndUser(longID(authentication), subPartyId);
	}


	public @RequestMapping(
			value = "/details/party",
			method = GET,
			produces = JSON
	) Long getPartyId(Authentication authentication,
					  @RequestParam("id") Long subPartyId) {
		return getSubParty(authentication, subPartyId).getParty();
	}


	public @RequestMapping(
			value = "/details/type",
			method = GET,
			produces = JSON
	) Type getSubPartyType(Authentication authentication,
						   @RequestParam("id") Long subPartyId) {
		return getSubParty(authentication, subPartyId).getType();
	}


	public @RequestMapping(
			value = "/details/users",
			method = GET,
			produces = JSON
	) Long[] getSubPartyUsers(Authentication authentication,
							  @RequestParam("id") Long subPartyId) {
		return getSubParty(authentication, subPartyId).getUsers().toArray(new Long[0]);
	}


	public @ResponseStatus(OK) @RequestMapping(
			value = "/leave",
			method = {POST, DELETE, GET}
	) void leaveSubParty(HttpServletResponse servletResponse) throws IOException {
		servletResponse.sendRedirect(EndpointAPI.GROUP + "/leave");
	}

}