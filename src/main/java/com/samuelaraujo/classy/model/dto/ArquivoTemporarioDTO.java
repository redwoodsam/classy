package com.samuelaraujo.classy.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class ArquivoTemporarioDTO {

    private String usuarioId;
    private MultipartFile arquivo;
    private String nomeModificado;
    private String contentType;

    public ArquivoTemporarioDTO() {
    }

    public ArquivoTemporarioDTO(Long usuarioId, String nomeModificado) {
        this.usuarioId = usuarioId.toString();
        this.nomeModificado = nomeModificado;
    }

    public ArquivoTemporarioDTO(Long usuarioId, String nomeModificado, MultipartFile arquivo) {
        this.usuarioId = usuarioId.toString();
        this.nomeModificado = nomeModificado;
        this.arquivo = arquivo;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
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
