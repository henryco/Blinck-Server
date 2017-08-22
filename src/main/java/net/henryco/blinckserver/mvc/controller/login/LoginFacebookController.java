package net.henryco.blinckserver.mvc.controller.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 21/08/17.
 */
@RestController
@RequestMapping("/login/facebook")
public class LoginFacebookController {


	@RequestMapping(method = GET)
	public String logIn(@RequestParam("token") String fbToken) {

		System.out.println(fbToken);
		return "";
	}

}
