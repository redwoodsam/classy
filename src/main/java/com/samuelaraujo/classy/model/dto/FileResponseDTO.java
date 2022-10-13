package com.samuelaraujo.classy.model.dto;

import org.springframework.core.io.Resource;

public class FileResponseDTO {

    private String nomeArquivo;
    private Resource resource;
    private String contentType;

    public FileResponseDTO(String nomeArquivo, Resource resource, String contentType) {
        this.nomeArquivo = nomeArquivo;
        this.resource = resource;
        this.contentType = contentType;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
