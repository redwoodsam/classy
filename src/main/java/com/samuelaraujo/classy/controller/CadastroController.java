package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.CadastroDto;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public String cadastro(Model model) {
		return "cadastro";
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String cadastrar(CadastroDto cadastroDto, Model model) {
		try {
			Usuario usuarioCadastrado = usuarioService.cadastrar(cadastroDto);
			return "forward:/";
		} catch (DadoInvalidoException e) {
			model.addAttribute("erro", e.getMessage());
			return "forward:cadastro";
		}
	}
	
}
