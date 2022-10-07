package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.service.AnuncioService;

@Controller
@RequestMapping("/anuncios")
public class AnuncioController {

	@Autowired
	private AnuncioService anuncioService;
	
	@GetMapping("/{id}")
	public String anuncio(@PathVariable Long id, Model model) {
		
		Anuncio anuncio = anuncioService.buscarPorId(id);
		
		model.addAttribute("anuncio", anuncio);
		return "anuncio";
	}
	
}
