package com.samuelaraujo.classy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.repository.AnuncioRepository;

@Service
public class AnuncioService {

	@Autowired
	private AnuncioRepository anuncioRepository;
	
	public List<Anuncio> listarTodos() {
		return anuncioRepository.findAll();
	}
	
	public Anuncio buscarPorId(Long id) {
		return anuncioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Anúncio não encontrado"));
	}

	public Page<Anuncio> listarPorEmailUsuario(String email, Pageable pageable) {
		return anuncioRepository.listarPorEmailUsuario(email, pageable);
	}
	
}
