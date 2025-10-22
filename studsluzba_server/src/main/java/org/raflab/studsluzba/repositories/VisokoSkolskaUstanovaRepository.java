package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.VisokoSkolskaUstanova;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisokoSkolskaUstanovaRepository extends CrudRepository<VisokoSkolskaUstanova, Long> {
}
