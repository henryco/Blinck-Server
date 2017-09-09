package net.henryco.blinckserver.mvc.controller.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * @author Henry on 09/09/17.
 */
@Controller
public class SimpleChatController {

	@MessageMapping("/test")
	@SendToUser("/queue/test")
	public String stompTest(String income, Authentication authentication) {
		System.out.println("STOMP TEST " + income);
		System.out.println("Auth: "+authentication);
		return "RESULT: " + income;
	}

}