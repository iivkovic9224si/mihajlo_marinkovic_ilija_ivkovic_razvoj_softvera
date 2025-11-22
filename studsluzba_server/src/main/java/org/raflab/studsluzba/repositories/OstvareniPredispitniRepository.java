package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.OstvareniPredispitni;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OstvareniPredispitniRepository extends CrudRepository<OstvareniPredispitni, Long> {
    @Query("SELECT op FROM OstvareniPredispitni op " +
            "JOIN op.slusaPredmet sp " +
            "JOIN op.predispitneObaveze po " +
            "WHERE sp.studentIndeks.id = :studentIndeksId " +
            "AND sp.drziPredmet.predmet.id = :predmetId " +
            "AND sp.skolskaGodina.godina = :skolskaGodina")
    List<OstvareniPredispitni> findOstvareniPoeniZaStudentaNaPredmetu(
            @Param("studentIndeksId") Long studentIndeksId,
            @Param("predmetId") Long predmetId,
            @Param("skolskaGodina") String skolskaGodina
    );

    List<OstvareniPredispitni> findBySlusaPredmet(SlusaPredmet slusaPredmet);
}
