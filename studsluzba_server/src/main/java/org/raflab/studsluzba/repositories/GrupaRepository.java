package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.Grupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Long> {
}
