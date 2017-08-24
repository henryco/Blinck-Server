package net.henryco.blinckserver.mvc.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Henry on 24/08/17.
 */
@RestController @RequestMapping("/admin")
public class AdminTestController {


	@RequestMapping(method = RequestMethod.GET, value = "/panel")
	public String hello() {

		return "hello";
	}
}
