package com.samuelaraujo.classy.exception;

public class NaoAutorizadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NaoAutorizadoException(String msg) {
		super(msg);
	}

}
