package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NastavnikZvanjeRepository extends JpaRepository<NastavnikZvanje, Long> {
    List<NastavnikZvanje> findByNastavnikId(Long nastavnikId);

    Optional<NastavnikZvanje> findByNastavnikIdAndAktivno(Long nastavnikId, boolean aktivno);

}
