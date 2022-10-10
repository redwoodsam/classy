package com.samuelaraujo.classy.model.dto;

import javax.validation.constraints.NotEmpty;

public class LoginDto {

	@NotEmpty(message = "O campo e-mail não pode ser vazio.")
	private String login;

	@NotEmpty(message = "O campo senha não pode ser vazio.")
	private String senha;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "LoginDto [login=" + login + ", senha=" + senha + "]";
	}

}
