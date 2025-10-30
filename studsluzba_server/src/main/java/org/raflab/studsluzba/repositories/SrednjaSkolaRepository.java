package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.SrednjaSkola;
import org.raflab.studsluzba.model.entity.VrstaSrednjeSkole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SrednjaSkolaRepository extends CrudRepository<SrednjaSkola, Long> {
    Optional<SrednjaSkola> findByNazivAndVrstaSrednjeSkole(String naziv, VrstaSrednjeSkole vrsta);
    Optional<SrednjaSkola> findBySifra(String sifra);

}
