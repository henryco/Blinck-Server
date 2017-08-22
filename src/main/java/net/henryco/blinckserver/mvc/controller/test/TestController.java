package net.henryco.blinckserver.mvc.controller.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Henry on 22/08/17.
 */
@RestController @RequestMapping("/test")
public class TestController {


	@RequestMapping(method = RequestMethod.GET)
	public String hello() {
		return "hello";
	}

}
