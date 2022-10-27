package com.samuelaraujo.classy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Foto;
import com.samuelaraujo.classy.model.FotoAnuncio;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

    @Query("select f from Foto f where f.nome=:nome")
    public Optional<Foto> buscarPorNome(String nome);

    @Query("select fa from FotoAnuncio fa where fa.foto.id=:idFoto")
    public Optional<FotoAnuncio> buscarFotoAnuncioPorIdFoto(Long idFoto);

    @Modifying
    @Query("delete from FotoAnuncio fa where fa.foto.id=:idFoto")
    public void apagarFotoAnuncioPorIdFoto(Long idFoto);

}
