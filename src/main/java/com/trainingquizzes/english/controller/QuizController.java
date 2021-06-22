package com.trainingquizzes.english.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("english")
public class QuizController {

	@GetMapping("quiz")
	public String quiz() {
		return "/english/quiz.xhtml";
	}
	
	@GetMapping("averages")
	public String averages() {
		return "/english/averages.xhtml";
	}
	
}
