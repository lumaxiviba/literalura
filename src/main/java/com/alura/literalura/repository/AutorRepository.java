// src/main/java/com/alura/literalura/repository/AutorRepository.java
package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.fechaFallecimiento IS NULL OR a.fechaFallecimiento > :anio")
    List<Autor> findAutoresVivosEnAnio(int anio);
    // AutorRepository.java
    Optional<Autor> findByNombre(String nombre);
}