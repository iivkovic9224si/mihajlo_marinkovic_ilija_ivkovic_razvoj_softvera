package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolozenPredmetRepository extends JpaRepository<PolozenPredmet, Long> {

    // selekcija svih položenih ispita za broj indeksa studenta
    @Query("select " +
            "p.predmet.sifra, p.predmet.naziv, p.predmet.opis, p.predmet.espb, p.predmet.obavezan " +
            "from PolozenPredmet p " +
            "where p.studentIndeks = :studentIndeks and p.ocena >= 6")
    List<Predmet> findPolozeniPredmetiByStudentIndeks(StudentIndeks studentIndeks);


    //prosečna ocena studenata na predmetu za raspon godina (na primer zadaje se godine 2015-2018 i vraća se prosečna ocena svih studenata koji su položili predmet u tom periodu)
    @Query("select AVG(p.ocena) from PolozenPredmet p where p.ocena >= 6 and p.predmet = :predmet and year(p.datum) between :donjaGodina and :gornjaGodina")
    Double getProsecnaOcenaZaPredmetIzmedju(@Param("predmet") Predmet predmet,
                                            @Param("donjaGodina") Integer donjaGodina,
                                            @Param("gornjaGodina") Integer gornjaGodina);

}
