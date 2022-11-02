package com.samuelaraujo.classy.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.ContaUsuarioDTO;
import com.samuelaraujo.classy.service.UsuarioService;
import com.samuelaraujo.classy.util.AutenticacaoUtil;

@Controller
@RequestMapping("/minha-conta")
public class MinhaContaController {
    
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Direciona o usuário à página Minha Conta para atualização cadastral
     */
    @GetMapping
    public String minhaConta(Model model) {
        if(!UsuarioService.isAuthenticated()) return "redirect:/";
        if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";

        Usuario usuarioLogado = AutenticacaoUtil.obterUsuarioLogado();
        Usuario usuarioCompleto = usuarioService.buscarPorId(usuarioLogado.getId());

        model.addAttribute("usuario", new ContaUsuarioDTO(usuarioCompleto));

        return "privadas/minha-conta";
    }

    /**
     * Direciona o usuário para a página para completar o cadastro (apenas se for seu primeiro acesso
     * após criar sua conta)
     */
    @GetMapping("finalizar-cadastro")
    public String finalizarCadastro(Model model) {
        if(!UsuarioService.isAuthenticated()) return "redirect:/";
        if(UsuarioService.isAuthenticated() && usuarioService.informacoesCompletas()) return "redirect:/";

        Usuario usuarioLogado = AutenticacaoUtil.obterUsuarioLogado();
        Usuario usuarioCompleto = usuarioService.buscarPorId(usuarioLogado.getId());

        model.addAttribute("usuario", new ContaUsuarioDTO(usuarioCompleto));

        return "privadas/finalizar-cadastro";
    }

    /**
     * Completa o cadastro do usuário com as informações de endereço e contato
     */
    @PostMapping("finalizar-cadastro")
    public String enviarDadosCompletos(@Valid ContaUsuarioDTO contaUsuarioDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(!UsuarioService.isAuthenticated()) return "redirect:/";

        usuarioService.atualizar(contaUsuarioDTO);

        if(result.hasErrors()) {
            return "redirect:/finalizar-cadastro";
        }

        redirectAttributes.addFlashAttribute("mensagem", "Dados salvos com sucesso");

        return "redirect:/";
    }

    /**
     * Atualiza o cadastro do usuário no sistema
     */
    @PostMapping
    public String atualizarCadastro(@Valid ContaUsuarioDTO contaUsuarioDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if(!UsuarioService.isAuthenticated()) return "redirect:/";

        usuarioService.atualizar(contaUsuarioDTO);

        if(result.hasErrors()) {
            return "redirect:/minha-conta";
        }

        redirectAttributes.addFlashAttribute("mensagem", "Dados salvos com sucesso");

        return "redirect:/minha-conta";
    }

    /**
     * Tratamento de erros
     */
    @ExceptionHandler(ConstraintViolationException.class)
	public String handleErrosValidacao(HttpServletRequest request, ConstraintViolationException ex, RedirectAttributes redirectAttributes) {

		List<String> mensagens = ex.getConstraintViolations().stream().map(violacao -> violacao.getMessage()).collect(Collectors.toList());

		redirectAttributes.addFlashAttribute("mensagens", mensagens);
		return String.format("redirect:%s",request.getRequestURL().toString());
	}

    @ExceptionHandler(DadoInvalidoException.class)
	public String handleDadoInvalido(HttpServletRequest request, DadoInvalidoException ex, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        System.out.println(request.getRequestURL().toString());
		return String.format("redirect:%s",request.getRequestURL().toString());
	}

}