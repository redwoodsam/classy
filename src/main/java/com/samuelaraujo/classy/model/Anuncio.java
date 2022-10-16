package com.samuelaraujo.classy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.samuelaraujo.classy.enums.StatusAnuncio;

import lombok.Data;

@Entity
@Data
public class Anuncio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@Lob
	@NotBlank
	private String descricao;

	@NotNull
	private BigDecimal valor;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL)
	private List<FotoAnuncio> fotos = new ArrayList<>();

	@Embedded
	private Thumbnail thumbnail;

	@Enumerated(EnumType.STRING)
	private StatusAnuncio statusAnuncio = StatusAnuncio.ABERTO;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPublicacao = LocalDate.now();


	public Thumbnail getThumbnail() {
		return this.thumbnail;
	}

	public void apagarThumbnail() {
		this.thumbnail = null;
	}

	public void setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setThumbnail(FotoAnuncio fotoAnuncio) {
		this.thumbnail = new Thumbnail(fotoAnuncio);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<FotoAnuncio> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoAnuncio> fotos) {
		this.fotos = fotos;
	}

	public StatusAnuncio getStatusAnuncio() {
		return statusAnuncio;
	}

	public void setStatusAnuncio(StatusAnuncio statusAnuncio) {
		this.statusAnuncio = statusAnuncio;
	}

	public void adicionarFoto(FotoAnuncio fotoAnuncio) {
		this.fotos.add(fotoAnuncio);
	}

	public LocalDate getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(LocalDate dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	@Override
	public String toString() {
		return "Anuncio [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", valor=" + valor + ", usuario="
				+ usuario + ", fotos=" + fotos + ", statusAnuncio=" + statusAnuncio + ", dataPublicacao="
				+ dataPublicacao + "]";
	}

}
