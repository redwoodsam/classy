package com.samuelaraujo.classy.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.samuelaraujo.classy.model.Usuario;

public class AutenticacaoUtil {
    
    public static Usuario obterUsuarioLogado() {
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return usuario;
        } catch(RuntimeException e) {
            return null;
        }
    }

}
