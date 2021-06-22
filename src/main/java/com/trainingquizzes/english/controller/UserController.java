package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("user")
public class UserController {

	@GetMapping("register")
	public String quiz() {
		return "/user/register.xhtml";
	}
	
	@GetMapping("password")
	public String password() {
		return "/user/password.xhtml";
	}
	
	@RequestMapping(value = "confirm-register", method = {RequestMethod.GET, RequestMethod.POST})
	public String confirmRegister() {
		return "/user/confirm-register.xhtml";
	}
	
	@RequestMapping(value = "android/confirm-register", method = {RequestMethod.GET, RequestMethod.POST})
	public String androidConfirmRegister() {
		return "/user/android/confirm-register.xhtml";
	}
	
	@RequestMapping(value = "reset-password", method = {RequestMethod.GET, RequestMethod.POST})
	public String confirmReset() {
		return "/user/reset-password.xhtml";
	}
	
	@RequestMapping(value = "android/reset-password", method = {RequestMethod.GET, RequestMethod.POST})
	public String androidConfirmReset() {
		return "/user/android/reset-password.xhtml";
	}
	
	@RequestMapping(value = "android/delete", method = {RequestMethod.GET, RequestMethod.POST})
	public String androidDeleteUser() {
		return "/user/android/delete-user.xhtml";
	}
	
}
