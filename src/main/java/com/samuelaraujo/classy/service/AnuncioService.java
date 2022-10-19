package com.samuelaraujo.classy.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.FotoAnuncio;
import com.samuelaraujo.classy.model.Thumbnail;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.AnuncioDTO;
import com.samuelaraujo.classy.model.dto.AnuncioRespostaDTO;
import com.samuelaraujo.classy.model.dto.ArquivoDTO;
import com.samuelaraujo.classy.model.dto.FileResponseDTO;
import com.samuelaraujo.classy.repository.AnuncioRepository;
import com.samuelaraujo.classy.util.ArquivoUtil;
import com.samuelaraujo.classy.util.AutenticacaoUtil;

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

	@Transactional
	public AnuncioRespostaDTO salvar(AnuncioDTO anuncioDTO) {

		Anuncio novoAnuncio = new Anuncio();
		novoAnuncio.setDataPublicacao(LocalDate.now());
		novoAnuncio.setNome(anuncioDTO.getTitulo());
		novoAnuncio.setDescricao(anuncioDTO.getDescricao());
		novoAnuncio.setValor(anuncioDTO.getValor());

		Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		novoAnuncio.setUsuario(usuarioLogado);

		return new AnuncioRespostaDTO(anuncioRepository.save(novoAnuncio));
	}

	@Transactional
	public Anuncio atualizar(Long id, AnuncioDTO anuncioDTO) {
		Anuncio anuncioSave = buscarPorId(id);

		if(anuncioDTO.getThumbnailId() != null && anuncioDTO.getThumbnailId() != 0) {
			FotoAnuncio thumbnail = fotoService.buscarFotoAnuncioPorIdFoto(anuncioDTO.getThumbnailId());
			anuncioSave.setThumbnail(new Thumbnail(thumbnail));
			validaAutoriaFotoAnuncio(anuncioSave, thumbnail);
		} else {
			anuncioSave.apagarThumbnail();
		}

		validaAutoria(anuncioSave);

		anuncioSave.setNome(anuncioDTO.getTitulo());
		anuncioSave.setDescricao(anuncioDTO.getDescricao());
		anuncioSave.setValor(anuncioDTO.getValor());
		anuncioSave.setStatusAnuncio(anuncioDTO.getStatusAnuncio());

		return anuncioRepository.save(anuncioSave);
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
		validaQuantidadeFotosMaxima(anuncio);

		String nomeModificado = ArquivoUtil.modificarNome(arquivo.getOriginalFilename());
		
		Foto novaFoto = new Foto();
		novaFoto.setFormato(arquivo.getContentType());
		novaFoto.setNome(nomeModificado);
		novaFoto.setPath(nomeModificado);
		novaFoto.setUsuarioId(AutenticacaoUtil.obterUsuarioLogado().getId());

		ArquivoDTO arquivoDto = new ArquivoDTO(anuncio.getUsuario().getId(), anuncio.getId(), nomeModificado, arquivo);
		fileSystemService.armazenarArquivo(arquivoDto);
		
		Foto fotoSave = fotoService.salvar(novaFoto);

		FotoAnuncio fotoAnuncio = new FotoAnuncio();
		fotoAnuncio.setAnuncio(anuncio);
		fotoAnuncio.setFoto(novaFoto);

		anuncio.adicionarFoto(fotoAnuncio);

		anuncioRepository.save(anuncio);

		return fotoSave;
	}

	// Apaga foto do anúncio
	@Transactional
	public void apagarFoto(Long idAnuncio, Long idFoto) {

		Anuncio anuncio = buscarPorId(idAnuncio);
		FotoAnuncio fotoAnuncio = fotoService.buscarFotoAnuncioPorIdFoto(idFoto);
		
		validaAutoria(anuncio);
		validaAutoriaFotoAnuncio(anuncio, fotoAnuncio);

		ArquivoDTO arquivoDto = new ArquivoDTO(anuncio.getUsuario().getId(), anuncio.getId(), fotoAnuncio.getFoto().getNome());
		fileSystemService.excluirFotoAnuncio(arquivoDto);

		if(anuncio.getThumbnail() != null && fotoAnuncio.getId() == anuncio.getThumbnail().getThumbnailId()) {
			resetaThumbnail(anuncio);
		}
		
		fotoService.apagarFotoAnuncio(idFoto);
	}
	

	@Transactional
	private void resetaThumbnail(Anuncio anuncio) {

		if(anuncio.getFotos().size() <= 1) {
			anuncio.apagarThumbnail();
		} else {
			FotoAnuncio fotoAnuncioAtual = fotoService.buscarFotoAnuncioPorIdFoto(anuncio.getThumbnail().getThumbnailId());
	
			int indiceThumbnail = anuncio.getFotos().indexOf(fotoAnuncioAtual);
			int indiceFotoSeguinte = obterIndiceSeguinte(indiceThumbnail, anuncio.getFotos());
			Long id = anuncio.getFotos().get(indiceFotoSeguinte).getFoto().getId();
	
			FotoAnuncio fotoNova = fotoService.buscarFotoAnuncioPorIdFoto(id);
	
			anuncio.setThumbnail(fotoNova);
	
			anuncioRepository.save(anuncio);
		}

	}

	private int obterIndiceSeguinte(int indiceAtual, List<?> lista) {
		int tamanho = lista.size();
		int proximoIndice = indiceAtual + 1;

		if(proximoIndice == tamanho) {
			return 0;
		}

		return proximoIndice;
	}

	// Verifica se o usuário autenticado é o dono do anúncio
	public void validaAutoria(Anuncio anuncio) {
		String emailAnuncio = anuncio.getUsuario().getDadosPessoais().getEmail();
		String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!emailAnuncio.equals(emailLogado)) {
			throw new NaoAutorizadoException("Acesso Negado");
		}
	}

	// Verifica se a foto realmente pertence ao anúncio
	public void validaAutoriaFotoAnuncio(Anuncio anuncio, FotoAnuncio fotoAnuncio) {
		if(fotoAnuncio.getAnuncio().getId() != anuncio.getId()) {
			throw new NaoAutorizadoException("Acesso Negado");
		}
	}

	// Verifica se o anúncio possui mais de 6 fotos(quantidade máxima)
	public void validaQuantidadeFotosMaxima(Anuncio anuncio) {
		if(anuncio.getFotos().size() >= 6) {
			throw new DadoInvalidoException("Você só pode adicionar 6 fotos por anúncio.");
		}
	}

}
