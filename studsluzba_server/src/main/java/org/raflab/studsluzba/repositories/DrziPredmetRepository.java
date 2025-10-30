package org.raflab.studsluzba.repositories;

import java.util.List;


import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DrziPredmetRepository extends CrudRepository<DrziPredmet, Long> {

    @Query("select dp.predmet from DrziPredmet dp " +
            "where dp.nastavnik.email = :nastavnikEmail and dp.skolskaGodina.aktivna = true")
    List<Predmet> getPredmetiZaNastavnikaUAktivnojSkolskojGodini(String nastavnikEmail);

    @Query("select dp from DrziPredmet dp where dp.nastavnik.id = :idNastavnik "
			+ "and dp.predmet.id = :idPredmet")
	DrziPredmet getDrziPredmetNastavnikPredmet(Long idPredmet, Long idNastavnik);

    List<DrziPredmet> findByPredmetAndSkolskaGodina(Predmet predmet, SkolskaGodina aktuelnaGodina);
}
