package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IspitRepository extends CrudRepository<Ispit, Long> {
    Optional<Ispit> findByDrziPredmetAndIspitniRok(DrziPredmet drziPredmet, IspitniRok ispitniRok);


}
