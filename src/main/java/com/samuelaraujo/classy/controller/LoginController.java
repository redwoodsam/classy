package com.samuelaraujo.classy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.samuelaraujo.classy.service.UsuarioService;
import com.samuelaraujo.classy.service.security.LoginAttemptService;
import com.samuelaraujo.classy.util.IpUtil;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginAttemptService loginAttemptService;

    /**
     * Direciona o usuário à página de login
     */
	@GetMapping
	public String login(@RequestParam(required = false) Long error, Model model) {

		boolean ipBloqueado = IpUtil.validaIpBloqueado(loginAttemptService, request);

		// Caso o IP esteja bloqueado, informar erro no front-end
		if(error != null) {
			if(error == 9 && ipBloqueado) {
				model.addAttribute("erro", "Muitas tentativas de acesso. Seu IP está bloqueado por 24 horas");
			}
		}

		// Mostrar tela de login apenas quando o usuário não está logado
		// Caso contrário, redireciona-o para a Home Page
		if(!UsuarioService.isAuthenticated()) return "login";
		
		return "redirect:/";
	}

}