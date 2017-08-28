package net.henryco.blinckserver.mvc.controller.secured.admin;

import net.henryco.blinckserver.mvc.controller.secured.BlinckProfileController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 24/08/17.
 */
@RestController
@RequestMapping("/protected/admin")
public class AdminProfileController implements BlinckProfileController {



	@Override
	public String profile() {
		return "wellcome admin";
	}



}
