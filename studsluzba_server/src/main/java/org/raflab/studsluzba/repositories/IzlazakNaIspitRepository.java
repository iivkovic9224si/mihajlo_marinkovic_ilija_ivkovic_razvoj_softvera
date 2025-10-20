package org.raflab.studsluzba.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<org.raflab.studsluzba.model.IzlazakNaIspit, Long> {
}
