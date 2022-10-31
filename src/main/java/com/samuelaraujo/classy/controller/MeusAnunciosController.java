package com.samuelaraujo.classy.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Categoria;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.AnuncioDTO;
import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.CategoriaService;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/meus-anuncios")
public class MeusAnunciosController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public String meusAnuncios(Model model, Pageable pageable) {

        if(!UsuarioService.isAuthenticated()) return "redirect:/";
        if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Anuncio> anuncios = anuncioService.listarPorEmailUsuario(usuarioLogado.getDadosPessoais().getEmail(), pageable);
        model.addAttribute("anuncios", anuncios);

        return "privadas/meus-anuncios";
    }

    @GetMapping("busca")
    public String pesquisaMeusAnuncios(@RequestParam("q") String pesquisa, Model model, Pageable pageable) {

        if(!UsuarioService.isAuthenticated()) return "redirect:/";
        if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";

        Page<Anuncio> anuncios = anuncioService.buscaAnuncioUsuario(pesquisa, pageable);
        model.addAttribute("anuncios", anuncios);

        return "privadas/meus-anuncios";
    }

    @GetMapping("/novo")
	public String novoAnuncio(@ModelAttribute("anuncioDto") AnuncioDTO anuncioDto, Model model) {
		if(!UsuarioService.isAuthenticated()) return "redirect:/login";
        if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";

        List<Categoria> categorias = categoriaService.listarTodas();

        model.addAttribute("categorias", categorias);

		return "privadas/novo-anuncio";
	}

    @ExceptionHandler(ConstraintViolationException.class)
	public String handleErrosValidacao(HttpServletRequest request, ConstraintViolationException ex, RedirectAttributes redirectAttributes) {

		List<String> mensagens = ex.getConstraintViolations().stream().map(violacao -> violacao.getMessage()).collect(Collectors.toList());

		redirectAttributes.addFlashAttribute("mensagens", mensagens);
		return String.format("redirect:%s",request.getRequestURL().toString());
	}

}
