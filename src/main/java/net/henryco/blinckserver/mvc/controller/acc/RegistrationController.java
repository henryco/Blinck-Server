package net.henryco.blinckserver.mvc.controller.acc;

import net.henryco.blinckserver.mvc.service.data.AdminDataService;
import net.henryco.blinckserver.security.credentials.AdminCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Henry on 31/08/17.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {


	private final AdminDataService adminDataService;

	@Autowired
	public RegistrationController(AdminDataService adminDataService) {
		this.adminDataService = adminDataService;
	}



	public @RequestMapping(
			method = POST,
			value = "/admin"
	) void registerAdmin(@RequestBody AdminCredentials credentials) {

		String user_id = credentials.getUser_id();
		String password = credentials.getPassword();

		adminDataService.addNewProfileIfNotExist(user_id, password);
	}



}