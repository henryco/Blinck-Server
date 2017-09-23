package net.henryco.blinckserver.mvc.controller.secured.user.infrastructure;

import net.henryco.blinckserver.mvc.controller.BlinckController;
import net.henryco.blinckserver.mvc.service.infrastructure.ReportAndBanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController // TODO: 19/09/17 TESTS
@RequestMapping(BlinckController.EndpointAPI.REPORT)
public class ReportUserController implements BlinckController {

	private final ReportAndBanService reportAndBanService;

	@Autowired
	public ReportUserController(ReportAndBanService reportAndBanService) {
		this.reportAndBanService = reportAndBanService;
	}


	/*
	 *	Meeting offer API
	 *
	 *		ENDPOINT: 		/protected/user/report
	 *
	 *
	 * 		REPORT:
	 *
	 * 			ENDPOINT:	/
	 * 			ARGS:		Long: id, String: reason
	 * 			METHOD:		POST
	 * 			RETURN:		BOOLEAN
	 *
	 */


	public @RequestMapping(
			value = "/",
			method = POST
	) Boolean reportUser(Authentication authentication,
						 @RequestParam("id") Long userId,
						 @RequestParam(value = "reason", required = false) String reason) {
		return reportAndBanService.report(longID(authentication), userId, reason);
	}

}