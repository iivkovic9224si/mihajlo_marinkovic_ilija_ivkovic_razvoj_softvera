package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IspitniRokRepository extends CrudRepository<IspitniRok, Long> {
    List<IspitniRok> findBySkolskaGodina(SkolskaGodina aktivna);
}
