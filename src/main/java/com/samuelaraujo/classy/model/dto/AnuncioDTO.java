package com.samuelaraujo.classy.model.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.samuelaraujo.classy.enums.StatusAnuncio;

public class AnuncioDTO implements Serializable {

    @NotBlank(message = "O campo título é obrigatório.")
    private String titulo;

    @NotNull(message = "O campo valor é obrigatório.")
    private String valor;

    @Lob
    @NotBlank(message = "O campo descrição é obrigatório.")
    private String descricao;

    private List<String> fotosId = new ArrayList<>();

    private Long thumbnailId;

    @Enumerated(EnumType.STRING)
    private StatusAnuncio statusAnuncio = StatusAnuncio.ABERTO;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        try {
            Currency moedaReal = Currency.getInstance("BRL");
            DecimalFormat formatador = new DecimalFormat("#,##0.00");
            formatador.setCurrency(moedaReal);

            this.valor = formatador.parse(valor).toString();
        } catch(ParseException e) {
            this.valor = "0";
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotosId() {
        return fotosId;
    }

    public void setFotosId(List<String> fotosId) {
        this.fotosId = fotosId;
    }

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public StatusAnuncio getStatusAnuncio() {
        return statusAnuncio;
    }

    public void setStatusAnuncio(StatusAnuncio statusAnuncio) {
        this.statusAnuncio = statusAnuncio;
    }

    @Override
    public String toString() {
        return "AnuncioDTO [titulo=" + titulo + ", valor=" + valor + ", descricao=" + descricao + ", fotosId=" + fotosId
                + ", thumbnailId=" + thumbnailId + ", statusAnuncio=" + statusAnuncio + "]";
    }

}