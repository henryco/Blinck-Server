package net.henryco.blinckserver.mvc.controller.secured;

import org.springframework.security.core.Authentication;


/**
 * @author Henry on 28/08/17.
 */
public interface BlinckProfileController {

	String profile();
	String[] permissions(Authentication authentication);

}