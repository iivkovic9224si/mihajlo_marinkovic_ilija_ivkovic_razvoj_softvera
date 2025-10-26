package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkolskaGodinaRepository extends CrudRepository<SkolskaGodina,Long> {
    SkolskaGodina findByAktivnaTrue();

    SkolskaGodina findByGodina(String godina);

    @Modifying
    @Query("UPDATE SkolskaGodina g SET g.aktivna = false")
    void deactivateAll();
}

