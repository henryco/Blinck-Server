package net.henryco.blinckserver.mvc.controller.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Henry on 09/09/17.
 */
@Controller
public class SimpleChatController {

	@MessageMapping("/test")
	public void stompTest(String income) {
		System.out.println("STOMP TEST " + income);
	}

}