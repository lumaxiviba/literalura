// src/main/java/com/alura/literalura/repository/LibroRepository.java
package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findByIdioma(Libro.Idioma idioma);

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor WHERE l.titulo ILIKE %:titulo%")
    List<Libro> buscarPorTitulo(String titulo);
}