package com.samuelaraujo.classy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.service.AnuncioService;

@Controller
@RequestMapping("/")
public class HomeController {

	
	@Autowired
	private AnuncioService anuncioService;
	
	@GetMapping
	public String home(Model model) {
		
		List<Anuncio> anuncios = anuncioService.listarTodos();
		model.addAttribute("anuncios", anuncios);
		
		return "home";
	}
}
