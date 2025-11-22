package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {

    @Query("SELECT AVG(p.ocena) FROM PolozenPredmet p WHERE p.izlazakIspita.prijavaIspita.ispit.id = :ispitId")
    Double getProsecnaOcenaNaIspituById(@Param("ispitId") Long ispitId);

    @Query("select count(ini) " +
            "from IzlazakNaIspit ini " +
            "join ini.prijavaIspita pi " +
            "join pi.slusaPredmet sp " +
            "where sp.studentIndeks.id = :studentId " +
            "and sp.drziPredmet.id = :drziPredmetId")
    Long countIzlasciZaStudentaNaPredmet(@Param("studentId") Long studentId,
                                         @Param("drziPredmetId") Long drziPredmetId);

    @Query("select " +
            "        s.ime," +
            "        s.prezime," +
            "        si.studProgramOznaka," +
            "        si.godina," +
            "        si.broj," +
            "        (ini.brojPoenaSaIspita + ini.brojPredispitnihPoena) * 1.0 " +
            "    from IzlazakNaIspit ini " +
            "        join ini.prijavaIspita pi " +
            "        join pi.slusaPredmet pr " +
            "        join pr.studentIndeks si " +
            "        join si.student s " +
            "    where pi.ispit.id = :ispitId " +
            "    order by si.studProgramOznaka, si.godina, si.broj")
    List<Object[]> getRezultatiIspitaRawByIspitId(Long ispitId);

    List<IzlazakNaIspit> findByPrijavaIspitaId(Long prijavaIspitaId);

}
