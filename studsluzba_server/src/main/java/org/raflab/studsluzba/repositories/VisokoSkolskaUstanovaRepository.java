package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.model.entity.VrstaVisokoSkolskihUstanova;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisokoSkolskaUstanovaRepository extends CrudRepository<VisokoSkolskaUstanova, Long> {
    Optional<VisokoSkolskaUstanova> findByNaziv(String naziv);

    List<VisokoSkolskaUstanova> findByNazivContainingIgnoreCaseAndVrsta(String naziv, VrstaVisokoSkolskihUstanova vrsta);

}
