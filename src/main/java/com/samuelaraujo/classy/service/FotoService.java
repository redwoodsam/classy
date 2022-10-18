package com.samuelaraujo.classy.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.FotoAnuncio;
import com.samuelaraujo.classy.model.Usuario;
import com.samuelaraujo.classy.model.dto.ArquivoTemporarioDTO;
import com.samuelaraujo.classy.repository.FotoRepository;
import com.samuelaraujo.classy.util.ArquivoUtil;
import com.samuelaraujo.classy.util.AutenticacaoUtil;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private FileSystemService fileSystemService;

	@Transactional
	public Foto uploadFotoAnuncioTemporaria(MultipartFile arquivo) {

		Usuario usuarioLogado = AutenticacaoUtil.obterUsuarioLogado();
		String nomeModificado = ArquivoUtil.modificarNome(arquivo.getOriginalFilename());

		Foto novaFoto = new Foto();
		novaFoto.setFormato(arquivo.getContentType());
		novaFoto.setNome(nomeModificado);
		novaFoto.setPath(nomeModificado);
		novaFoto.setUsuarioId(usuarioLogado.getId());

		ArquivoTemporarioDTO arquivoTemporarioDTO = new ArquivoTemporarioDTO(usuarioLogado.getId(), nomeModificado,
				arquivo);
		fileSystemService.armazenarArquivo(arquivoTemporarioDTO);

		try {
			salvar(novaFoto);
			return novaFoto;
		} catch (RuntimeException e) {
			fileSystemService.excluirArquivoTemporario(arquivoTemporarioDTO);
			throw new RuntimeException("Erro ao salvar foto. Por gentileza tente novamente.");
		}
	}

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
	public void apagar(Long id) {
		Foto fotoSave = buscarPorId(id);
		validaAutoria(fotoSave);

		ArquivoTemporarioDTO arquivoTemporarioDTO = new ArquivoTemporarioDTO(fotoSave.getUsuarioId(), fotoSave.getNome());
		fileSystemService.excluirArquivoTemporario(arquivoTemporarioDTO);

		fotoRepository.delete(fotoSave);
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
