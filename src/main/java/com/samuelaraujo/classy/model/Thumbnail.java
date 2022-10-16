package com.samuelaraujo.classy.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Thumbnail implements Serializable {

    private Long thumbnailId;
    private String thumbnailNome;

    public Thumbnail(FotoAnuncio fotoAnuncio) {
        this.thumbnailId = fotoAnuncio.getFoto().getId();
        this.thumbnailNome = fotoAnuncio.getFoto().getPath();
    }

    public Thumbnail() {
    }

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getThumbnailNome() {
        return thumbnailNome;
    }

    public void setThumbnailNome(String thumbnailNome) {
        this.thumbnailNome = thumbnailNome;
    }

}
