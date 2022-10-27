package com.samuelaraujo.classy.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class EnvioThumbnailDTO implements Serializable {
    
    @NotNull
    private Long thumbnailId;

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

}
