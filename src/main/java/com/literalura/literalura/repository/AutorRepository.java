package com.literalura.literalura.repository;

import com.literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.anoNacimiento <= :anoBuscado AND a.anoFallecimiento >= :anoBuscado")
    List<Autor> autoresVivosEnAno(Integer anoBuscado);

    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE :nombreAutorBuscado")
    List<Autor> buscarAutorPorNombre(String nombreAutorBuscado);
}
