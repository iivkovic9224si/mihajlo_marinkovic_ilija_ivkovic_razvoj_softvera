package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozenPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolozenPredmetRepository extends JpaRepository<PolozenPredmet, Long> {
}
