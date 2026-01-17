package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PredmetRepository extends CrudRepository<Predmet, Long> {

    @Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
    List<Predmet> getPredmetForGodinaAkreditacije(Integer godinaAkreditacije);

    List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);

    List<Predmet> findByIdIn(List<Long> ids);
    List<Predmet> findByNazivIn(List<String> nazivi);

    Optional<Predmet> findBySifra(String sifra);

    boolean existsBySifra(String sifra); // ✅ DODATO

    List<Predmet> findByStudProgram(StudijskiProgram sp);

    @Query("SELECT p FROM Predmet p WHERE " +
            "p.godinaStudija <= :maxGodina AND " +
            "p.studProgram.oznaka = :studProgramOznaka AND " +
            "p.obavezan = true")
    List<Predmet> findDostupniZaUpis(@Param("maxGodina") Integer maxGodina,
                                     @Param("studProgramOznaka") String studProgramOznaka);

    List<Predmet> findByStudProgramOznakaAndObavezan(String oznakaStudProgram, boolean obavezan);
}
