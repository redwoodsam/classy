package com.samuelaraujo.classy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Anuncio;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long>{

    @Query("select a from Anuncio a join a.usuario u where u.dadosPessoais.email = :email")
    public Page<Anuncio> listarPorEmailUsuario(String email, Pageable pageable);

}
