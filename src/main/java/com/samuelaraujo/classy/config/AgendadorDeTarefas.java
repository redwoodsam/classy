package com.samuelaraujo.classy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.FileSystemService;
import com.samuelaraujo.classy.service.FotoService;
import com.samuelaraujo.classy.service.security.LoginAttemptService;

@Component
@EnableScheduling
public class AgendadorDeTarefas {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private FileSystemService fileSystemService;
    
    @Autowired
    private FotoService fotoService;
    
    @Autowired
    private LoginAttemptService loginAttemptService;

    private final String CRON = "0 0 0 * * *";

    @Scheduled(cron = CRON, zone = "America/Cuiaba")
    public void apagarTudo() {
        loginAttemptService.resetaContador();
    }
}
