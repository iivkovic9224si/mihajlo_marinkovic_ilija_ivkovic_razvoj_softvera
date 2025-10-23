package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrijavaIspitaRepository extends CrudRepository<PrijavaIspita, Long> {
    //svi prijavljeni studenti za ispit
    @Query("select sp.studentIndeks.student from PrijavaIspita pi " +
            "join pi.slusaPredmet sp "+
            "where pi.ispit = :ispit")
    List<StudentPodaci> getPrijavljenjiStudentiZaIspit(Ispit ispit);

    //za datog studenta i predmet vratiti koliko je puta student polagao predmet (izašao na ispit)
    @Query("select count(pi.izlazakNaIspita) from PrijavaIspita pi where pi.slusaPredmet.studentIndeks = :studentIndeks and pi.slusaPredmet.drziPredmet.predmet = :predmet")
    Long getBrojIzlazakaStudentaNaIspitIzPredmeta(StudentIndeks studentIndeks, Predmet predmet);
}
