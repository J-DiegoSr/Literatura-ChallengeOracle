package com.alura.challenge.Literatura_ChallengeOracle.repository;

import com.alura.challenge.Literatura_ChallengeOracle.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibroRepository extends JpaRepository <Libro,Long> {
    @Query("SELECT l FROM Libro l WHERE l.language ILIKE %:language%")
    List<Libro> findByLanguage(String language);

    @Query("SELECT l FROM Libro l ORDER BY l.download_count DESC")
    List<Libro> findTop10ByOrderByDownloadCountDesc(Pageable pageable);
}
