package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PropertiesController {
	
	@RequestMapping("/properties")
	public String index() {
		return "properties.xhtml";
	}
	
}
