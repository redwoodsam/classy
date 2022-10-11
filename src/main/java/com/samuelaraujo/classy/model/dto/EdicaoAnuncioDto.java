package com.samuelaraujo.classy.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EdicaoAnuncioDto implements Serializable {

    private String nome;
    private String descricao;
    private BigDecimal valor;
    private String fotoPrincipal;
    private List<Long> fotos = new ArrayList<>();
    private boolean fechado = false;

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

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public void setFotoPrincipal(String fotoPrincipal) {
        this.fotoPrincipal = fotoPrincipal;
    }

    public List<Long> getFotos() {
        return fotos;
    }

    public void setFotos(List<Long> fotos) {
        this.fotos = fotos;
    }

    public boolean isFechado() {
        return fechado;
    }

    public void setFechado(boolean fechado) {
        this.fechado = fechado;
    }

    @Override
    public String toString() {
        return "EdicaoAnuncioDto [nome=" + nome + ", descricao=" + descricao + ", valor=" + valor + ", fotoPrincipal="
                + fotoPrincipal + ", fotos=" + fotos + ", fechado=" + fechado + "]";
    }

}
