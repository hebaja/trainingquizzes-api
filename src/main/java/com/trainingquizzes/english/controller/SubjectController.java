package com.trainingquizzes.english.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("english/subjects")
public class SubjectController {
	
	@GetMapping("register_subject")
	public String subject() {
		return "/english/subjects/register_subject.xhtml";
	}

	@GetMapping("list_subjects")
	public String loadSubject() {
		return "/english/subjects/list_subjects.xhtml";
	}
	
}
