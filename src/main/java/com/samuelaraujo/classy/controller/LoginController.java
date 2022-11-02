package com.samuelaraujo.classy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * Direciona o usuário à página de login
     */
	@GetMapping
	public String login(Model model) {
		if(!UsuarioService.isAuthenticated()) return "login";
		
		return "redirect:/";
	}
	
}
