package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.Predmet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredispitneObavezeRepository extends CrudRepository<PredispitneObaveze,Long> {
    List<PredispitneObaveze> findByPredmet(Predmet predmet);
}
