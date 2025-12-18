package org.raflab.studsluzba.repositories;

import enums.VrstaSrednjeSkole;
import org.raflab.studsluzba.model.entity.SrednjaSkola;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SrednjaSkolaRepository extends CrudRepository<SrednjaSkola, Long> {
    Optional<SrednjaSkola> findByNazivAndVrstaSrednjeSkole(String naziv, VrstaSrednjeSkole vrsta);
    Optional<SrednjaSkola> findBySifra(String sifra);

}
