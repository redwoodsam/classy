package com.samuelaraujo.classy.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.exception.DadoInvalidoException;
import com.samuelaraujo.classy.model.Anuncio;
import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.dto.AnuncioDTO;
import com.samuelaraujo.classy.model.dto.AnuncioRespostaDTO;
import com.samuelaraujo.classy.model.dto.EnvioThumbnailDTO;
import com.samuelaraujo.classy.model.dto.FileResponseDTO;
import com.samuelaraujo.classy.service.AnuncioService;
import com.samuelaraujo.classy.service.UsuarioService;

@Controller
@RequestMapping("/anuncios")
public class AnuncioController {

	@Autowired
	private AnuncioService anuncioService;
	
	@GetMapping("/{id}")
	public String anuncio(@PathVariable Long id, Model model) {
		
		Anuncio anuncio = anuncioService.buscarPorId(id);
		
		model.addAttribute("anuncio", anuncio);
		return "anuncio";
	}

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

	@GetMapping("/{id}/edit")
	public String anuncioEdit(@PathVariable Long id, Model model) {
		
		if(!UsuarioService.isAuthenticated()) return "redirect:/login";
		Anuncio anuncioSave = anuncioService.buscarPorId(id);

		model.addAttribute("anuncio", anuncioSave);
		return "privadas/editar-anuncio";
	}

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

	@PostMapping("/{id}/upload")
	public ResponseEntity<Foto> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		
		Foto foto = anuncioService.salvarFoto(id, file);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(foto);
	}

	@PostMapping("/{id}/thumbnail")
	public ResponseEntity<?> atualizaThumbnail(@PathVariable Long id, @Valid EnvioThumbnailDTO thumbnail) {
		
		System.out.println(thumbnail.getThumbnailId());
		anuncioService.setarThumbnail(id, thumbnail.getThumbnailId());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{idAnuncio}/fotos/{idFoto}")
	public ResponseEntity<?> apagarImagem(@PathVariable Long idAnuncio, @PathVariable Long idFoto) {
		
		anuncioService.apagarFoto(idAnuncio, idFoto);

		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping("/{idAnuncio}/{nomeFoto}")
	public ResponseEntity<Resource> downloadImagem(@PathVariable Long idAnuncio, @PathVariable String nomeFoto) {
		
		FileResponseDTO foto = anuncioService.obterFoto(idAnuncio, nomeFoto);

		return ResponseEntity
			.ok()
			.contentType(MediaType.parseMediaType(foto.getContentType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + foto.getNomeArquivo() + "\"")
			.body(foto.getResource());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public String handleErrosValidacao(HttpServletRequest request, ConstraintViolationException ex, RedirectAttributes redirectAttributes) {

		List<String> mensagens = ex.getConstraintViolations().stream().map(violacao -> violacao.getMessage()).collect(Collectors.toList());

		redirectAttributes.addFlashAttribute("mensagens", mensagens);
		return String.format("redirect:%s",request.getRequestURL().toString());
	}
	
}
