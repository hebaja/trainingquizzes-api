package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class RedirectController {
	
	@GetMapping
	public RedirectView redirectRoot() {
		return new RedirectView("https://trainingquizzes.herokuapp.com/");
	}
	
	@GetMapping("about")
	public RedirectView redirectAbout() {
		return new RedirectView("https://trainingquizzes.herokuapp.com/#/about");
	}

}
