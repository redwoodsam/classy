package com.samuelaraujo.classy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Anuncio;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long>{

}
