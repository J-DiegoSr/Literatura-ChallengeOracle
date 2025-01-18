package com.alura.challenge.Literatura_ChallengeOracle.repository;

import com.alura.challenge.Literatura_ChallengeOracle.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository  extends JpaRepository <Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.diaCumpleanos <= :year AND (a.diaFallecio IS NULL OR a.diaFallecio >= :year) ORDER BY a.diaCumpleanos ASC")
    List<Autor> findAuthorsAliveInYear(Integer year);

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Autor> findByName(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.diaCumpleanos = :year")
    List<Autor> findByBirthYear(Integer year);

    @Query("SELECT a FROM Autor a WHERE a.diaFallecio = :year")
    List<Autor> findByDeathYear(Integer year);

    boolean existsByNombre(String nombre); // Cambio realizado aquí
}
