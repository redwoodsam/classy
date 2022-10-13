package com.samuelaraujo.classy.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class ArquivoDTO {

    private String usuarioId;
    private String anuncioId;
    private MultipartFile arquivo;
    private String nomeModificado;
    private String contentType;

    public ArquivoDTO() {
    }

    public ArquivoDTO(Long usuarioId, Long anuncioId, String nomeModificado) {
        this.usuarioId = usuarioId.toString();
        this.anuncioId = anuncioId.toString();
        this.nomeModificado = nomeModificado;
    }

    public ArquivoDTO(Long usuarioId, Long anuncioId, MultipartFile arquivo) {
        this.usuarioId = usuarioId.toString();
        this.anuncioId = anuncioId.toString();
        this.arquivo = arquivo;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setAnuncioId(String anuncioId) {
        this.anuncioId = anuncioId;
    }

    public String getNomeModificado() {
        return nomeModificado;
    }

    public void setNomeModificado(String nomeModificado) {
        this.nomeModificado = nomeModificado;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId.toString();
    }

    public String getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(Long anuncioId) {
        this.anuncioId = anuncioId.toString();
    }

    public MultipartFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(MultipartFile arquivo) {
        this.arquivo = arquivo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
