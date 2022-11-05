package com.samuelaraujo.classy.service.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    // Número máximo de tentativas de login
    private final int NUMERO_MAXIMO = 5;
    private Map<String, Integer> numeroTentativas = new HashMap<>();

    // Caso o login seja bem sucedido, remove o IP da "lista negra"
    public void loginSucedido(String ip) {
        numeroTentativas.remove(ip);
    }

    // Caso o login seja mal sucedido, incrementa o número de tentativas
    public void loginFracassado(String ip) {
        int attempts = 0;
        try {
            attempts = numeroTentativas.get(ip);
        } catch (RuntimeException e) {
            attempts = 0;
        }
        attempts++;
        numeroTentativas.put(ip, attempts);
    }
    
    public void resetaContador() {
        numeroTentativas.clear();
    }

    public boolean estaBloqueado(String ip) {
        try {
            return numeroTentativas.get(ip) >= NUMERO_MAXIMO;
        } catch (RuntimeException e) {
            return false;
        }
    }
}