package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.dto.CadastroDto;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping(value = "/cadastro")
public class CadastroController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public String cadastro(Model model) {
		return "cadastro";
	}
	
	@PostMapping(value = "/registro", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String cadastrar(@RequestBody CadastroDto cadastroDto) {
		usuarioService.cadastrar(cadastroDto);
		System.out.println(cadastroDto.getNomeCompleto());
		
		return "forward:home";
	}
	
}
