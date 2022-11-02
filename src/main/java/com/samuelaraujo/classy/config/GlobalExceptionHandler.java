package com.samuelaraujo.classy.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;
/**
 * 
 * Classe responsável por tratar todos os erros genéricos que possam ocorrer na aplicação
 * @author Samuel Araújo
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String tamanhoMaximoUpload;

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleNaoEncontrado(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(value = NaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleNaoEncontrado(NaoEncontradoException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(value = NaoAutorizadoException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<?> handleNaoAutorizado(NaoAutorizadoException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}

	@ExceptionHandler(FileSizeLimitExceededException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> handleErroTamanhoMaximo(HttpServletRequest request, FileSizeLimitExceededException ex, RedirectAttributes redirectAttributes) {
		String mensagem = String.format("Arquivo muito grande. Tamanho máximo permitido: %s", tamanhoMaximoUpload);

		redirectAttributes.addFlashAttribute("mensagem", mensagem);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagem);
	}
	
	
}
