package com.samuelaraujo.classy.service;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.FotoAnuncio;
import com.samuelaraujo.classy.model.dto.ArquivoDTO;
import com.samuelaraujo.classy.model.dto.FileResponseDTO;
import com.samuelaraujo.classy.repository.AnuncioRepository;
import com.samuelaraujo.classy.util.ArquivoUtil;

@Service
public class AnuncioService {

	@Autowired
	private AnuncioRepository anuncioRepository;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private FileSystemService fileSystemService;
	
	public List<Anuncio> listarTodos() {
		return anuncioRepository.findAll();
	}
	
	public Anuncio buscarPorId(Long id) {
		return anuncioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Anúncio não encontrado"));
	}

	public Page<Anuncio> listarPorEmailUsuario(String email, Pageable pageable) {
		return anuncioRepository.listarPorEmailUsuario(email, pageable);
	}

	public FileResponseDTO obterFoto(Long idAnuncio, String nomeFoto) {

		Anuncio anuncioSave = buscarPorId(idAnuncio);
		Foto fotoSave = fotoService.buscarPorNome(nomeFoto);

		ArquivoDTO arquivoDTO = new ArquivoDTO(anuncioSave.getUsuario().getId(), anuncioSave.getId(), fotoSave.getNome());

		Resource resource = fileSystemService.obterArquivoSalvo(arquivoDTO);

		return new FileResponseDTO(fotoSave.getNome(), resource, fotoSave.getFormato());
	}

	@Transactional
	public Foto salvarFoto(Long idAnuncio, MultipartFile arquivo) {

		Anuncio anuncio = buscarPorId(idAnuncio);
		
		validaAutoria(anuncio);

		System.out.println(arquivo);

		String nomeModificado = ArquivoUtil.modificarNome(arquivo.getOriginalFilename());
		
		Foto novaFoto = new Foto();
		novaFoto.setFormato(arquivo.getContentType());
		novaFoto.setNome(nomeModificado);
		novaFoto.setPath(nomeModificado);

		ArquivoDTO arquivoDto = new ArquivoDTO(anuncio.getUsuario().getId(), anuncio.getId(), arquivo);
		fileSystemService.armazenarArquivo(arquivoDto);
		
		Foto fotoSave = fotoService.salvar(novaFoto);

		FotoAnuncio fotoAnuncio = new FotoAnuncio();
		fotoAnuncio.setAnuncio(anuncio);
		fotoAnuncio.setFoto(fotoSave);

		anuncio.adicionarFoto(fotoAnuncio);

		anuncioRepository.save(anuncio);

		return fotoSave;
	}
	
	// Verifica se o usuário autenticado é o dono do anúncio
	public void validaAutoria(Anuncio anuncio) {
		String emailAnuncio = anuncio.getUsuario().getDadosPessoais().getEmail();
		String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!emailAnuncio.equals(emailLogado)) {
			throw new NaoAutorizadoException("Acesso Negado");
		}
	}

}
