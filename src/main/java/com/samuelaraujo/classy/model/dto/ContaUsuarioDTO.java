package com.samuelaraujo.classy.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samuelaraujo.classy.model.DadosPessoais;
import com.samuelaraujo.classy.model.Endereco;
import com.samuelaraujo.classy.model.Usuario;

public class ContaUsuarioDTO implements Serializable {

    private Long id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String nomeCompleto;

    @NotBlank
    private String email;

    @NotBlank
    private String telefone;

    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    private String senha = "";
    private String senha2 = "";

    @JsonIgnore
    private String nome;
    private String sobrenome;

    public ContaUsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nomeCompleto = String.format("%s %s", usuario.getDadosPessoais().getNome(), usuario.getDadosPessoais().getSobrenome());
        this.email = usuario.getDadosPessoais().getEmail();
        this.telefone = usuario.getDadosPessoais().getTelefone();
        this.endereco = usuario.getDadosPessoais().getEndereco() != null ? usuario.getDadosPessoais().getEndereco().getEndereco() : "";
        this.numero = usuario.getDadosPessoais().getEndereco() != null ? usuario.getDadosPessoais().getEndereco().getNumero() : "";
        this.complemento = usuario.getDadosPessoais().getEndereco() != null? usuario.getDadosPessoais().getEndereco().getComplemento() : "";
        this.bairro = usuario.getDadosPessoais().getEndereco() != null ? usuario.getDadosPessoais().getEndereco().getBairro() : "";
        this.cidade = usuario.getDadosPessoais().getEndereco() != null ? usuario.getDadosPessoais().getEndereco().getCidade() : "";
        this.uf = usuario.getDadosPessoais().getEndereco() != null ? usuario.getDadosPessoais().getEndereco().getUf() : "";
        this.senha = usuario.getPassword();
    }

    public ContaUsuarioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        String nome = "";

        String[] nomes = this.nomeCompleto.split(" ");
        nome = nomes[0];
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        String sobrenome = "";

        String[] nomes = this.nomeCompleto.split(" ");
        if(nomes.length > 1) {
            sobrenome = nomes[1];
        }

        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario obterUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setPassword(this.senha);
        
        DadosPessoais dadosPessoais = new DadosPessoais();
        dadosPessoais.setEmail(this.email);
        dadosPessoais.setNome(getNome());
        dadosPessoais.setSobrenome(getSobrenome());
        dadosPessoais.setTelefone(this.telefone);
        
        Endereco endereco = new Endereco();
        endereco.setEndereco(this.endereco);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setUf(this.uf);

        dadosPessoais.setEndereco(endereco);
        usuario.setDadosPessoais(dadosPessoais);

        return usuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getSenha2() {
        return senha2;
    }

    public void setSenha2(String senha2) {
        this.senha2 = senha2;
    }

    @Override
    public String toString() {
        return "ContaUsuarioDTO [id=" + id + ", nomeCompleto=" + nomeCompleto + ", email=" + email + ", telefone="
                + telefone + ", endereco=" + endereco + ", numero=" + numero + ", complemento=" + complemento
                + ", bairro=" + bairro + ", cidade=" + cidade + ", uf=" + uf + ", senha=" + senha + "]";
    }

}
