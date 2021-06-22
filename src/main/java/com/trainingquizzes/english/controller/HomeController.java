package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "home.xhtml";
	}
	
	@RequestMapping("/hello")
	public String hello() {
		return "/hello.xhtml";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "/login.xhtml";
	}
	
	@RequestMapping("/policy")
	public String policy() {
		return "/policy.xhtml";
	}
}
