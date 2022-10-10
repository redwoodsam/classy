package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/meus-anuncios")
public class MeusAnunciosController {

    @Autowired
    private AnuncioService anuncioService;
    
    @GetMapping
    public String meusAnuncios(Model model, Pageable pageable) {

        if(!UsuarioService.isAuthenticated()) {
            return "redirect:/";
        }

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Anuncio> anuncios = anuncioService.listarPorEmailUsuario(usuarioLogado.getDadosPessoais().getEmail(), pageable);
        model.addAttribute("anuncios", anuncios);

        return "privadas/meus-anuncios";
    }

}
