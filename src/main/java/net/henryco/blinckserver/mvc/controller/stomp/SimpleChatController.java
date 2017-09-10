package net.henryco.blinckserver.mvc.controller.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * @author Henry on 09/09/17.
 */
@Controller
public class SimpleChatController {


	private final SimpMessagingTemplate template;


	@Autowired
	public SimpleChatController(SimpMessagingTemplate template) {
		this.template = template;
		new Thread(() -> {
			while (true) {
				template.convertAndSend("/message/wow", "ALL TEST");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	public @MessageMapping("/test")
//	@SendToUser("/queue/test") String
	void stompTest(String income, Authentication authentication) {
		System.out.println("\nSTOMP TEST " + income);
		System.out.println("Auth: "+authentication);

		template.convertAndSendToUser(
				authentication.getName(), // user
				"/queue/test",
				"RESULT: " + income
		);
	}




}