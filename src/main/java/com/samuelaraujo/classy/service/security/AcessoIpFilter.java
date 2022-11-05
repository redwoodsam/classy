package com.samuelaraujo.classy.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.samuelaraujo.classy.util.IpUtil;

public class AcessoIpFilter extends OncePerRequestFilter {

    private LoginAttemptService loginAttemptService;

    public AcessoIpFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        boolean ipBloqueado = IpUtil.validaIpBloqueado(loginAttemptService, request);

        if(ipBloqueado && request.getRequestURI().equals("/login") && request.getMethod().toUpperCase().equals("POST")) {
            response.sendRedirect("/login?error=9");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    
}
