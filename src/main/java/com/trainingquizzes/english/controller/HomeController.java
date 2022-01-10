package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "home.xhtml";
	}
			
	@RequestMapping("/policy")
	public String policy() {
		return "/policy.xhtml";
	}
	
	@RequestMapping("/populatedb")
	public String populatedb() {
		return "/populatedb.xhtml";
	}
}
