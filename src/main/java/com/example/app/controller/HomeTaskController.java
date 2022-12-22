package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/homeTask")
public class HomeTaskController {

	@GetMapping
	public String createGet(Model model) throws Exception {
		return "homeTask";
	}
	
}
