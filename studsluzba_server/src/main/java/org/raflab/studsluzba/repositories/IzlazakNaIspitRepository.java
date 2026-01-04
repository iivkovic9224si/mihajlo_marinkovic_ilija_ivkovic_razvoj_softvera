package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.raflab.studsluzba.model.entity.PrijavaIspita;
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

    @Query(
            "select pi " +
                    "from PrijavaIspita pi " +
                    "join pi.slusaPredmet sp " +
                    "join pi.ispit i " +
                    "where sp.studentIndeks.godina = :godina " +
                    "and sp.studentIndeks.broj = :broj " +
                    "and sp.studentIndeks.studProgramOznaka = :oznaka " +
                    "and i.datumIspita >= CURRENT_DATE " +
                    "and pi.izlazakNaIspita is null"
    )
    List<PrijavaIspita> findPrijaveZaIzlazakZaStudenta(@Param("godina") Integer godina,
                                                       @Param("broj") Integer broj,
                                                       @Param("oznaka") String oznaka);


    // 1) Svi izlasci za datog studenta
    @Query("select ini from IzlazakNaIspit ini " +
            "join ini.prijavaIspita pi " +
            "join pi.slusaPredmet sp " +
            "where sp.studentIndeks.godina = :godina " +
            "and sp.studentIndeks.broj = :broj " +
            "and sp.studentIndeks.studProgramOznaka = :oznaka")
    List<IzlazakNaIspit> findIzlasciZaStudenta(@Param("godina") Integer godina,
                                               @Param("broj") Integer broj,
                                               @Param("oznaka") String oznaka);

    // 2) Da ne može 2x izlazak za istu prijavu (najjednostavnije)
    boolean existsByPrijavaIspitaId(Long prijavaIspitaId);

}
