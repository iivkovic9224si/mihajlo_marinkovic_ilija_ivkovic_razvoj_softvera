package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrijavaIspitaRepository extends CrudRepository<PrijavaIspita, Long> {
    //svi prijavljeni studenti za ispit
    @Query("select p.slusaPredmet from PrijavaIspita p where p.ispit.id = :ispitId")
    List<SlusaPredmet> findSlusaPredmetByIspitId(Long ispitId);

    //za datog studenta i predmet vratiti koliko je puta student polagao predmet (izašao na ispit)
    @Query("select count(pi.izlazakNaIspita) from PrijavaIspita pi where pi.slusaPredmet.studentIndeks = :studentIndeks and pi.slusaPredmet.drziPredmet.predmet = :predmet")
    Long getBrojIzlazakaStudentaNaIspitIzPredmeta(StudentIndeks studentIndeks, Predmet predmet);
}
