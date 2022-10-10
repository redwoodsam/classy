package com.samuelaraujo.classy.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CadastroDto {

	@NotBlank(message = "O campo nome completo não pode ser vazio.")
	private String nomeCompleto;
	
	@Email(message = "E-mail inválido")
	@NotBlank(message = "O campo e-mail não pode ser vazio.")
	private String email;
	
	@NotBlank(message = "O campo senha não pode ser vazio.")
	private String senha;
	
	@NotBlank(message = "O campo confirmar senha não pode ser vazio.")
	private String senha2;

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

}
