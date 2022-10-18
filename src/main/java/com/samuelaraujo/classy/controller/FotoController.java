package com.samuelaraujo.classy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.service.FotoService;

@Controller
@RequestMapping("/fotos")
public class FotoController {
    
    @Autowired
    private FotoService fotoService;

    @PostMapping("/anuncio/temp")
    public ResponseEntity<Foto> uploadFotoTemporaria(@RequestParam("file") MultipartFile arquivo) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(fotoService.uploadFotoAnuncioTemporaria(arquivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> uploadFotoTemporaria(Long id) {

        fotoService.apagar(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
