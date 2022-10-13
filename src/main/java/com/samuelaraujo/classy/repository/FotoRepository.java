package com.samuelaraujo.classy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

    @Query("select f from Foto f where f.nome=:nome")
    public Optional<Foto> buscarPorNome(String nome);

}
