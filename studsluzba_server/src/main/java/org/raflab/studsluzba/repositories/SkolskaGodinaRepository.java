package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SkolskaGodinaRepository extends CrudRepository<SkolskaGodina,Long> {
    SkolskaGodina findByAktivnaTrue();

    SkolskaGodina findByGodina(String godina);
    Optional<SkolskaGodina> findOneByGodina(String godina);
    @Transactional
    @Modifying
    @Query("UPDATE SkolskaGodina g SET g.aktivna = false")
    void deactivateAll();
}

