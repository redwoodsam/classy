package com.samuelaraujo.classy.enums;

public enum StatusAnuncio {

	ABERTO("Aberto"),
	FECHADO("Fechado");
	
	private String descricao;
	
	StatusAnuncio(String descricao) {
		this.descricao = descricao;
	}
	
}
