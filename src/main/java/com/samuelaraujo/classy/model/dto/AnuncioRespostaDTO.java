package com.samuelaraujo.classy.model.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.samuelaraujo.classy.model.Anuncio;

public class AnuncioRespostaDTO implements Serializable {

    private Long id;

    @NotBlank(message = "O campo título é obrigatório.")
    private String titulo;

    @NotNull(message = "O campo valor é obrigatório.")
    private String valor;

    @Lob
    @NotBlank(message = "O campo descrição é obrigatório.")
    private String descricao;

    public AnuncioRespostaDTO(Anuncio anuncio) {
        this.id = anuncio.getId();
        this.titulo = anuncio.getNome();
        this.valor = anuncio.getValor().toString();
        this.descricao = anuncio.getDescricao();
    }

    public AnuncioRespostaDTO() {
    }

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
            // Define os símbolos para o formato brasileiro.
            // (vírgula para separador decimal e ponto para separador de milhar)
            DecimalFormatSymbols regiaoBR = new DecimalFormatSymbols(new Locale("pt", "BR"));

            // Configura o formatador
            DecimalFormat formatador = new DecimalFormat("#,##0.00");
            formatador.setDecimalFormatSymbols(regiaoBR);

            this.valor = formatador.parse(valor).toString();
        } catch (ParseException e) {
            this.valor = "0";
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
