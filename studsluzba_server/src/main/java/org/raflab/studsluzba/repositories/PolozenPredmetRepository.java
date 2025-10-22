package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozenPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PolozenPredmetRepository extends JpaRepository<PolozenPredmet, Long> {

    //selekcija svih položenih ispita za broj indeksa studenta
    @Query("select p.predmet from PolozenPredmet p where p.studentIndeks = :studentIndeks and p.ocena >= 6")
    List<Predmet> findPolozeniPredmetiByStudentIndeks(long studentIndeks);

    //prosečna ocena studenata na predmetu za raspon godina (na primer zadaje se godine 2015-2018 i vraća se prosečna ocena svih studenata koji su položili predmet u tom periodu)
    @Query("select AVG(p.ocena) from PolozenPredmet p where p.ocena >= 6 and p.predmet = :predmet and year(p.datum) between :donjaGodina and :gornjaGodina")
    Double getProsecnaOcenaZaPredmetIzmedju(Predmet predmet, Integer donjaGodina, Integer gornjaGodina);
}
