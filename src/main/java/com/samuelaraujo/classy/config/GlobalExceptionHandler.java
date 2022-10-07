package com.samuelaraujo.classy.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.samuelaraujo.classy.exception.NaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleNaoEncontrado(RuntimeException e) {
		return ResponseEntity.badRequest().build();
	}
	
	@ExceptionHandler(value = NaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleNaoEncontrado(NaoEncontradoException e) {
		return ResponseEntity.notFound().build();
	}
	
}
