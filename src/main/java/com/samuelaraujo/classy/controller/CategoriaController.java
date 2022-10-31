package com.samuelaraujo.classy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.samuelaraujo.classy.model.Categoria;
import com.samuelaraujo.classy.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @ResponseBody
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }
}
