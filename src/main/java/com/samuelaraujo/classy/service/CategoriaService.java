package com.samuelaraujo.classy.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Categoria;
import com.samuelaraujo.classy.repository.CategoriaRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Transactional
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional
    // Busca a categoria pelo nome de URL (slug)
    public Categoria buscarPorSlug(String slug) {
        return categoriaRepository.buscarPorSlug(slug).orElseThrow(() -> new NaoEncontradoException("Categoria não encontrada"));
    }

}
