package net.henryco.blinckserver.mvc.controller.open;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Henry on 22/08/17.
 */ @Controller
@RequestMapping("/")
public class RootController {


	public @RequestMapping(
			method = GET
	) String main() {
		return "redirect:/public";
	}


	public @RequestMapping(
			method = GET,
			value = "/user"
	) String user() {
		return "redirect:/protected/user/profile";
	}


	public @RequestMapping(
			method = GET,
			value = "/admin"
	) String admin() {
		return "redirect:/protected/admin/profile";
	}


}