package com.samuelaraujo.classy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.dto.CadastroDto;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

	@GetMapping
	public String cadastro(Model model) {
		
		return "cadastro";
	}
	
	@PostMapping
	public String cadastrar(@RequestBody CadastroDto cadastroDto) {
		
		
		return "redirect:/cadastro";
	}
	
}
