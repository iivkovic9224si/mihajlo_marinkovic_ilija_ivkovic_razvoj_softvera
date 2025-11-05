package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {

    //prosečna ocena na ispitu
    @Query("select AVG(i.ocena) from IzlazakNaIspit i where i.prijavaIspita.ispit = :ispit")
    Double getProsecnaOcenaNaIspitu(Ispit ispit);

    //rezultati ispita u vidu sortiranog spiska studenata (sortirati prvo po stud programu, zatim po godini upisa, pa po broju) sa ukupnim brojem poena na ispitu (predispitne obaveze plus poeni sa ispita)
    @Query("select " +
            "        s.ime," +
            "        s.prezime," +
            "        si.studProgramOznaka," +
            "        si.godina," +
            "        si.broj," +
            "        (ini.brojPoenaSaIspita + ini.brojPredispitnihPoena) * 1.0" +
            "    from IzlazakNaIspit ini " +
            "        join ini.prijavaIspita pi " +
            "        join pi.slusaPredmet pr " +
            "        join pr.studentIndeks si " +
            "        join si.student s " +
            "    where pi.ispit = :ispit" +
            "    order by si.studProgramOznaka, si.godina, si.broj" +
            "")
    List<Object[]> getRezultatiIspitaRaw(Ispit ispit);
}
