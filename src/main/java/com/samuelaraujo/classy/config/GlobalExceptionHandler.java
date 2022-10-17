package com.samuelaraujo.classy.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samuelaraujo.classy.exception.NaoAutorizadoException;
import com.samuelaraujo.classy.exception.NaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// @ExceptionHandler(value = RuntimeException.class)
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// public ResponseEntity<?> handleNaoEncontrado(RuntimeException e) {
	// 	return ResponseEntity.badRequest().build();
	// }
	
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
	
}
