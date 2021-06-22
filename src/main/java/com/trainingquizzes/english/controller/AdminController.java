package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("config")
	public String index() {
		return "/admin/config.xhtml";
	}
	
	
}
