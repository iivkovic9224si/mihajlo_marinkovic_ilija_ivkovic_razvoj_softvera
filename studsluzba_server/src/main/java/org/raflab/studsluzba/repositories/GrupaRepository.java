package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Long> {
    @Query("SELECT g FROM Grupa g LEFT JOIN FETCH g.predmeti WHERE g.id = :id")
    Optional<Grupa> findByIdWithPredmeti(@Param("id") Long id);
}
