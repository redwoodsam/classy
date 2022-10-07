package com.samuelaraujo.classy.exception;

public class NaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NaoEncontradoException(String msg) {
		super(msg);
	}

}
