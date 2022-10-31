package com.samuelaraujo.classy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samuelaraujo.classy.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    @Query("select c from Categoria c where c.slug = :slug")
    public Optional<Categoria> buscarPorSlug(String slug);

}
