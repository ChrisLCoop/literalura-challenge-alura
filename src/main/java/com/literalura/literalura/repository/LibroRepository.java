package com.literalura.literalura.repository;

import com.literalura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l")
    List<Libro> listarLibrosRegistrados();

    @Query("SELECT l FROM Libro l WHERE l.idioma LIKE :idiomaElegido")
    List<Libro> listarLibrosPorIdioma(String idiomaElegido);

    @Query("SELECT l FROM Libro l WHERE l.autor ILIKE %:autorABuscar%")
    List<Libro> listarLibrosPorAutor(String autorABuscar);
}
