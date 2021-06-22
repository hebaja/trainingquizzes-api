package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("english")
public class SubjectController {
	
	@GetMapping("subject")
	public String subject() {
		return "/english/subject.xhtml";
	}

	@GetMapping("load_subject")
	public String loadSubject() {
		return "/english/load_subject.xhtml";
	}
	
}
