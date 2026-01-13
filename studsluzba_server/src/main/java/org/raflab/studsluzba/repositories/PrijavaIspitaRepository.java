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

    @Query("select p from PrijavaIspita p where p.slusaPredmet.studentIndeks.godina = :godina " +
            "and p.slusaPredmet.studentIndeks.broj = :broj " +
            "and p.slusaPredmet.studentIndeks.studProgramOznaka = :oznaka")
    List<PrijavaIspita> findByStudentIndeks(Integer godina, Integer broj, String oznaka);

    // ✅ NOVO: Dostupni ispiti za studenta (na kojima NIJE prijavljen)
    @Query("select i from Ispit i where i.drziPredmet.id in " +
            "(select dp.id from DrziPredmet dp join SlusaPredmet sp on dp.id = sp.drziPredmet.id " +
            "where sp.studentIndeks.godina = :godina " +
            "and sp.studentIndeks.broj = :broj " +
            "and sp.studentIndeks.studProgramOznaka = :oznaka) " +
            "and i.id not in (select pi.ispit.id from PrijavaIspita pi where pi.slusaPredmet.studentIndeks.godina = :godina " +
            "and pi.slusaPredmet.studentIndeks.broj = :broj " +
            "and pi.slusaPredmet.studentIndeks.studProgramOznaka = :oznaka)")
    List<Ispit> findDostupniIspitiZaStudenta(Integer godina, Integer broj, String oznaka);

    @Query("select distinct i from Ispit i " +
            "join i.drziPredmet dp " +
            "join SlusaPredmet sp on sp.drziPredmet = dp " +
            "where sp.studentIndeks.godina = :godina " +
            "and sp.studentIndeks.broj = :broj " +
            "and sp.studentIndeks.studProgramOznaka = :oznaka " +
            "and i.datumIspita >= CURRENT_DATE " +
            "and dp.predmet not in (" +
            "   select pp.predmet from PolozenPredmet pp " +
            "   where pp.studentIndeks.godina = :godina " +
            "   and pp.studentIndeks.broj = :broj " +
            "   and pp.studentIndeks.studProgramOznaka = :oznaka" +
            ") " +
            "and i.id not in (" +  // ✅ DODAJ OVO
            "   select pi.ispit.id from PrijavaIspita pi " +
            "   where pi.slusaPredmet.studentIndeks.godina = :godina " +
            "   and pi.slusaPredmet.studentIndeks.broj = :broj " +
            "   and pi.slusaPredmet.studentIndeks.studProgramOznaka = :oznaka" +
            ")")
    List<Ispit> findDostupniNepolozeniNepretekliIspiti(Integer godina, Integer broj, String oznaka);


    @Query("select (count(pi) > 0) from PrijavaIspita pi " +
            "where pi.ispit.id = :ispitId " +
            "and pi.slusaPredmet.studentIndeks.godina = :godina " +
            "and pi.slusaPredmet.studentIndeks.broj = :broj " +
            "and pi.slusaPredmet.studentIndeks.studProgramOznaka = :oznaka")
    boolean existsPrijavaZaStudentaIspit(Long ispitId, Integer godina, Integer broj, String oznaka);

    @Query("select pi from PrijavaIspita pi join pi.ispit i join pi.slusaPredmet sp " +
            "where sp.studentIndeks.godina = :godina and sp.studentIndeks.broj = :broj " +
            "and sp.studentIndeks.studProgramOznaka = :oznaka " +
            "and i.datumIspita >= CURRENT_DATE " +
            "and pi.id not in (select ini.prijavaIspita.id from IzlazakNaIspit ini)")
    List<PrijavaIspita> findPrijaveZaIzlazakZaStudenta(Integer godina, Integer broj, String oznaka);

    List<PrijavaIspita> findBySlusaPredmetAndIspit(SlusaPredmet slusaPredmet, Ispit ispit);

    boolean existsBySlusaPredmetAndIspit(SlusaPredmet slusaPredmet, Ispit ispit);


}
