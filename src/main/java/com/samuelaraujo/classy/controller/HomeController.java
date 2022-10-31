package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/")
public class HomeController {

	
	@Autowired
	private AnuncioService anuncioService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public String home(Model model, Pageable pageable) {
		
		if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";
		
		Page<Anuncio> anuncios = anuncioService.listarTodos(pageable);
		model.addAttribute("anuncios", anuncios);
		
		return "home";
	}

}
