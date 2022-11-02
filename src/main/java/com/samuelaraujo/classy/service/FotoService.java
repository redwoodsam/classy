package com.samuelaraujo.classy.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.FotoAnuncio;
import com.samuelaraujo.classy.repository.FotoRepository;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;

	@Autowired
	private UsuarioService usuarioService;

	public Foto buscarPorId(Long id) {
		return fotoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Foto não encontrada"));
	}

	public Foto buscarPorNome(String nome) {
		return fotoRepository.buscarPorNome(nome).orElseThrow(() -> new NaoEncontradoException("Foto não encontrada"));
	}

	public FotoAnuncio buscarFotoAnuncioPorIdFoto(Long idFoto) {
		return fotoRepository.buscarFotoAnuncioPorIdFoto(idFoto)
				.orElseThrow(() -> new NaoEncontradoException("Foto não encontrada"));
	}

	@Transactional
	public void apagarFotoAnuncio(Long id) {
		FotoAnuncio fotoAnuncio = buscarFotoAnuncioPorIdFoto(id);
		fotoRepository.apagarFotoAnuncioPorIdFoto(fotoAnuncio.getFoto().getId());
		fotoRepository.deleteById(fotoAnuncio.getFoto().getId());
	}

	@Transactional
	public Foto salvar(Foto foto) {
		return fotoRepository.save(foto);
	}

	// Verifica se o usuário autenticado é o dono da foto
	public void validaAutoria(Foto foto) {
		String emailAnuncio = usuarioService.buscarPorId(foto.getUsuarioId()).getDadosPessoais().getEmail();
		String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!emailAnuncio.equals(emailLogado)) {
			throw new NaoAutorizadoException("Acesso Negado");
		}
	}

}
