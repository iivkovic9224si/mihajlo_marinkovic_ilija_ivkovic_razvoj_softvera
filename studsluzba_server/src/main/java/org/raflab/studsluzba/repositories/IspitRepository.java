package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.Ispit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IspitRepository extends CrudRepository<Ispit, Long> {
}
