package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/minha-conta")
public class MinhaContaController {
    
    @GetMapping
    public String minhaConta(Model model) {
        if(!UsuarioService.isAuthenticated()) {
            return "redirect:/";
        }

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("usuario", usuarioLogado);

        return "privadas/minha-conta";
    }

}
