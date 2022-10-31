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

    @Query("select a from Anuncio a join a.categoria c where c.slug = :slug")
    public Page<Anuncio> listarPorCategoria(String slug, Pageable pageable);

    @Query("select a from Anuncio a where Upper(a.nome) like CONCAT('%', Upper(:busca), '%')")
    public Page<Anuncio> buscaAnuncio(String busca, Pageable pageable);

    @Query("select a from Anuncio a join a.usuario u where u.dadosPessoais.email = :email "
     + "and Upper(a.nome) like CONCAT('%', Upper(:busca), '%')")
    public Page<Anuncio> buscaAnuncioUsuario(String email, String busca, Pageable pageable);

}
