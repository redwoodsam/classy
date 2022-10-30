package com.samuelaraujo.classy.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String cadastro(CadastroDto cadastroDto) {
		return "cadastro";
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String cadastrar(@Valid CadastroDto cadastroDto, BindingResult result, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.cadastrar(cadastroDto);
			if(result.hasErrors()) {
				return "cadastro";
			}

			redirectAttributes.addFlashAttribute("mensagem", "Sua conta foi criada com sucesso");
			return "redirect:/login";

		} catch (DadoInvalidoException e) {
			ObjectError error = new ObjectError("email", e.getMessage());
			result.addError(error);
			return "cadastro";
		}
	}
	
}
