package com.samuelaraujo.classy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.repository.FotoRepository;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;
	
	
	public Foto buscarPorId(Long id) {
		return fotoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Foto não encontrada"));
	}
	
}
