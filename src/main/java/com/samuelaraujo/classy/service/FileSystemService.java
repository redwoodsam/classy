package com.samuelaraujo.classy.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.samuelaraujo.classy.exception.NaoEncontradoException;
import com.samuelaraujo.classy.model.dto.ArquivoDTO;
import com.samuelaraujo.classy.model.dto.ArquivoTemporarioDTO;

@Service
public class FileSystemService {

	@Autowired
	private ApplicationContext applicationContext;

	// Obtém o diretório raiz onde são guardados os arquivos
	// declarado no application.properties
	@Value("${uploads.path}")
	private String diretorioArquivos;

	// Salva arquivo físico em uma pasta do servidor
	public ArquivoDTO armazenarArquivo(ArquivoDTO arquivoDTO) {
		Path pathArquivos = Paths.get(diretorioArquivos);
		Path pathUsuario = Paths.get(arquivoDTO.getUsuarioId());
		Path pathAnuncio = Paths.get(arquivoDTO.getAnuncioId());

		File pastaUsuario = new File(pathArquivos.resolve(pathUsuario).toString());
		File pastaAnuncio = new File(pathArquivos.resolve(pathUsuario).resolve(pathAnuncio).toString());

		// Cria a pasta do usuário com o seu ID caso não existir
		if (!pastaUsuario.exists()) {
			pastaUsuario.mkdir();
		}

		// Cria a pasta do Anúncio com o seu ID caso não existir
		if (!pastaAnuncio.exists()) {
			pastaAnuncio.mkdir();
		}

		// Salva o arquivo para a pasta
		try (InputStream inputStream = arquivoDTO.getArquivo().getInputStream()) {
			Files.copy(inputStream, pastaAnuncio.toPath().resolve(arquivoDTO.getNomeModificado()), StandardCopyOption.REPLACE_EXISTING);
			return arquivoDTO;
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro ao salvar arquivo: %s", e.getMessage()));
		}
	}

	public ArquivoTemporarioDTO armazenarArquivo(ArquivoTemporarioDTO arquivoTemporarioDTO) {
		Path pathArquivos = Paths.get(diretorioArquivos);
		Path pathUsuario = Paths.get(arquivoTemporarioDTO.getUsuarioId());
		Path pathTemp = Paths.get("temp");

		File pastaUsuario = new File(pathArquivos.resolve(pathUsuario).toString());
		File pastaAnuncio = new File(pathArquivos.resolve(pathUsuario).resolve(pathTemp).toString());

		// Cria a pasta do usuário com o seu ID caso não existir
		if (!pastaUsuario.exists()) {
			pastaUsuario.mkdir();
		}

		// Cria a pasta do Anúncio com o seu ID caso não existir
		if (!pastaAnuncio.exists()) {
			pastaAnuncio.mkdir();
		}

		// Salva o arquivo para a pasta
		try (InputStream inputStream = arquivoTemporarioDTO.getArquivo().getInputStream()) {
			Files.copy(inputStream, pastaAnuncio.toPath().resolve(arquivoTemporarioDTO.getNomeModificado()), StandardCopyOption.REPLACE_EXISTING);
			return arquivoTemporarioDTO;
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro ao salvar arquivo: %s", e.getMessage()));
		}
	}

	// Busca um arquivo salvo no servidor
	public Resource obterArquivoSalvo(ArquivoDTO arquivoDTO) {
		Path pathUsuario = Paths.get(arquivoDTO.getUsuarioId());
		Path pathAnuncio = Paths.get(arquivoDTO.getAnuncioId());

		Path pathArquivo = Paths.get(diretorioArquivos).resolve(pathUsuario).resolve(pathAnuncio).resolve(arquivoDTO.getNomeModificado());

		// Caso o arquivo não for encontrado, gerar erro
		if (!Files.exists(pathArquivo)) {
			throw new NaoEncontradoException("Arquivo não encontrado.");
		}

		// Retorna o recurso de URI de onde está o arquivo
		try {
			return new UrlResource(pathArquivo.toUri());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Erro ao obter arquivo: " + e.getMessage());
		}
	}

	// Apaga o arquivo físico no servidor (Usado como rollback caso ocorra alguma
	// falha na persistência)
	public void excluirArquivo(ArquivoDTO arquivoDTO) {
		Path pathArquivos = Paths.get(diretorioArquivos);
		Path pathUsuario = Paths.get(arquivoDTO.getUsuarioId());
		Path pathAnuncio = Paths.get(arquivoDTO.getAnuncioId());
		File pastaUsuario = new File(pathArquivos.resolve(pathUsuario).resolve(pathAnuncio).toString());

		if (!pastaUsuario.exists()) {
			pastaUsuario.mkdir();
		}

		try {
			Files.deleteIfExists(pastaUsuario.toPath().resolve(arquivoDTO.getNomeModificado()));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro ao apagar arquivo: %s", e.getMessage()));
		}
	}

	// Apaga o arquivo físico no servidor (Usado como rollback caso ocorra alguma
	// falha na persistência)
	public void excluirArquivoTemporario(ArquivoTemporarioDTO arquivoTemporarioDTO) {
		Path pathArquivos = Paths.get(diretorioArquivos);
		Path pathUsuario = Paths.get(arquivoTemporarioDTO.getUsuarioId());
		Path pathTemp = Paths.get("temp");
		File pastaUsuario = new File(pathArquivos.resolve(pathUsuario).resolve(pathTemp).toString());

		if (!pastaUsuario.exists()) {
			pastaUsuario.mkdir();
		}

		try {
			Files.deleteIfExists(pastaUsuario.toPath().resolve(arquivoTemporarioDTO.getNomeModificado()));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro ao apagar arquivo: %s", e.getMessage()));
		}
	}

	// Apaga o arquivo físico no servidor
	public void excluirFotoAnuncio(ArquivoDTO arquivoDTO) {
		Path pathArquivos = Paths.get(diretorioArquivos);
		Path pathUsuario = Paths.get(arquivoDTO.getUsuarioId());
		Path pathAnuncio = Paths.get(arquivoDTO.getAnuncioId());
		File pastaUsuario = new File(pathArquivos.resolve(pathUsuario).resolve(pathAnuncio).toString());

		if (!pastaUsuario.exists()) {
			pastaUsuario.mkdir();
		}

		try {
			Files.deleteIfExists(pastaUsuario.toPath().resolve(arquivoDTO.getNomeModificado()));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Erro ao apagar arquivo: %s", e.getMessage()));
		}
	}

}
