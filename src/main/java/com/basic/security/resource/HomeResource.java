package com.basic.security.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {
	
	private Logger logger = LoggerFactory.getLogger( HomeResource.class );
	
	@GetMapping("/")
	public String home() {
		return "U have logged in successfully";
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome home";
	}	
	
	@GetMapping("/user")
	public String user() {
		return "welcome user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "welcome admin";
	}

}
