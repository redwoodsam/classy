package com.samuelaraujo.classy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

}
