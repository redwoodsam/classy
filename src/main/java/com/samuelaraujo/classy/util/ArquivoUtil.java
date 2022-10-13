package com.samuelaraujo.classy.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

public class ArquivoUtil {
    
    public static String obterExtensao(String nomeOriginal) {
        return FilenameUtils.getExtension(nomeOriginal);
    }

    public static String modificarNome(String nomeOriginal) {
        String nomeBase = FilenameUtils.getBaseName(nomeOriginal);
        String uuid = UUID.randomUUID().toString();
        String extensao = obterExtensao(nomeOriginal);
        return String.format("%s_%s.%s", nomeBase, uuid, extensao);
    }

}
