package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.OstvareniPredispitni;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OstvareniPredispitniRepository extends CrudRepository<OstvareniPredispitni, Long> {
}
