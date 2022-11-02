package com.samuelaraujo.classy.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Categoria;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.dto.AnuncioDTO;
import com.samuelaraujo.classy.model.dto.AnuncioRespostaDTO;
import com.samuelaraujo.classy.model.dto.EnvioThumbnailDTO;
import com.samuelaraujo.classy.model.dto.FileResponseDTO;
import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.CategoriaService;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/anuncios")
public class AnuncioController {

	@Autowired
	private AnuncioService anuncioService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CategoriaService categoriaService;

	/**
	 * Direciona o usuário para a view completa de um anúncio
	 * @param id do anúncio
	 * @return view anunio
	 */
	@GetMapping("/{id}")
	public String anuncio(@PathVariable Long id, Model model) {
		if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";
		Anuncio anuncio = anuncioService.buscarPorId(id);
		
		model.addAttribute("anuncio", anuncio);
		return "anuncio";
	}

	/**
     * Direciona o usuário para a view de lista de anúncios de uma determinada categoria
     * @param id do anúncio
     */
	@GetMapping("/categorias/{slug}")
	public String homeCategorizada(@PathVariable String slug, Model model, Pageable pageable) {
		
		if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";
		
		Page<Anuncio> anuncios = anuncioService.listarTodosPorCategoria(slug, pageable);
		model.addAttribute("anuncios", anuncios);
		
		return "home";
	}

	/**
	 * Realiza a busca de anúncios a partir de uma entrada do usuário
	 * @param Parâmetro de busca do usuário
	 * @return view home com anúncios encontrados pela busca
	 */
	@GetMapping("busca")
    public String pesquisaAnuncios(@RequestParam("q") String pesquisa, Model model, Pageable pageable) {

		if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";

        Page<Anuncio> anuncios = anuncioService.buscaAnuncio(pesquisa, pageable);
        model.addAttribute("anuncios", anuncios);

        return "home";
    }

	/**
	 * Cria um novo anúncio 
	 * @param anuncioDTO
	 */
	@PostMapping
	public ResponseEntity<?> cadastrar(@Valid AnuncioDTO anuncioDTO, BindingResult result) {
		try {
			AnuncioRespostaDTO anuncio = anuncioService.salvar(anuncioDTO);

			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(anuncio);

		} catch (DadoInvalidoException e) {
			return ResponseEntity
				.badRequest()
				.body(e.getMessage());
		}
	}

	/**
	 * Direciona o usuário para a tela de edição de um determinado anúncio
	 * @param Id do anúncio
	 */
	@GetMapping("/{id}/edit")
	public String anuncioEdit(@PathVariable Long id, Model model) {
		
		if(!UsuarioService.isAuthenticated()) return "redirect:/login";
		if(UsuarioService.isAuthenticated() && !usuarioService.informacoesCompletas()) return "redirect:/minha-conta/finalizar-cadastro";
		Anuncio anuncioSave = anuncioService.buscarPorId(id);
		List<Categoria> categorias = categoriaService.listarTodas();

		model.addAttribute("anuncio", anuncioSave);
		model.addAttribute("categorias", categorias);
		return "privadas/editar-anuncio";
	}

	/**
	 * Realiza a atualização de um determinado anúncio
	 * @param Id do anúncio
	 */
	@PostMapping("/{id}/edit")
	public String atualizarAnuncio(@PathVariable Long id, @Valid AnuncioDTO anuncioDto, BindingResult result, RedirectAttributes redirectAttributes) {
		
		anuncioService.atualizar(id, anuncioDto);
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("mensagem", "Falha ao atualizar anúncio");
			return String.format("redirect:/%d/edit", id);
		}

		redirectAttributes.addFlashAttribute("mensagem", "Anúncio atualizado com sucesso");

		return "redirect:/meus-anuncios";
	}

	/**
	 * Realiza o upload de uma imagem e a amarra a um anúncio
	 * @param Id do anúncio
	 * @param Arquivo de imagem
	 */
	@PostMapping("/{id}/upload")
	public ResponseEntity<Foto> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		
		Foto foto = anuncioService.salvarFoto(id, file);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(foto);
	}

	/**
	 * Seta uma nova thumbnail para um determinado anúncio
	 * @param Id do anúncio
	 * @param thumbnail
	 */
	@PostMapping("/{id}/thumbnail")
	public ResponseEntity<?> atualizaThumbnail(@PathVariable Long id, @Valid EnvioThumbnailDTO thumbnail) {
		anuncioService.setarThumbnail(id, thumbnail.getThumbnailId());
		return ResponseEntity.noContent().build();
	}

	
	/**
	 * Exclui uma determinada imagem de um anúncio
	 * @param Id do anúncio
	 * @param Id da foto a apagar
	 */
	@DeleteMapping("/{idAnuncio}/fotos/{idFoto}")
	public ResponseEntity<?> apagarImagem(@PathVariable Long idAnuncio, @PathVariable Long idFoto) {
		
		anuncioService.apagarFoto(idAnuncio, idFoto);

		return ResponseEntity
			.noContent()
			.build();
	}

	/**
	 * Apaga um anúncio
	 * @param Id do anúncio
	 */
	@DeleteMapping("/{idAnuncio}")
	public ResponseEntity<?> apagarAnuncio(@PathVariable Long idAnuncio) {
		
		anuncioService.apagarAnuncio(idAnuncio);

		return ResponseEntity
			.noContent()
			.build();
	}

	/**
	 * Realiza o download de uma foto do anúncio
	 * @param Id do anúncio
	 * @param Nome da foto
	 */
	@GetMapping("/{idAnuncio}/{nomeFoto}")
	public ResponseEntity<Resource> downloadImagem(@PathVariable Long idAnuncio, @PathVariable String nomeFoto) {
		FileResponseDTO foto = anuncioService.obterFoto(idAnuncio, nomeFoto.toString());

		return ResponseEntity
			.ok()
			.contentType(MediaType.parseMediaType(foto.getContentType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + foto.getNomeArquivo() + "\"")
			.body(foto.getResource());
	}

	/**
	 * Trata os erros de campos faltando e retorna as mensagens ao usuário
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleErrosValidacao(HttpServletRequest request, ConstraintViolationException ex, RedirectAttributes redirectAttributes) {

		List<String> mensagens = ex.getConstraintViolations().stream().map(violacao -> violacao.getMessage()).collect(Collectors.toList());

		redirectAttributes.addFlashAttribute("mensagens", mensagens);
		return String.format("redirect:%s",request.getRequestURL().toString());
	}

}
