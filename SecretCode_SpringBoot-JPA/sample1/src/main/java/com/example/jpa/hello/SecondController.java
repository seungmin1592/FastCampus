package com.example.jpa.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {
	

	@RequestMapping("/hello-spring")
	public String helloSpring() {
		
		return "hello Spring";
	}
	
	@GetMapping("/hello-rest")
	public String helloRest() {
		
		return "hello Rest";
	}
	
	@GetMapping("/api/helloworld")
	public String helloRestApi() {
		
		return "hello Rest API";
	}

}
