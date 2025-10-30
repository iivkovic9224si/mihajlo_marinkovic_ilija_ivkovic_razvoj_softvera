package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.OstvareniPredispitni;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OstvareniPredispitniRepository extends CrudRepository<OstvareniPredispitni, Long> {
    //selekcija ostvarenih poena na predispinim obavezama za studenta na određenom predmetu u školskoj godini
    @Query("select op.ostvareniPoeni from OstvareniPredispitni op "+
            "join op.slusaPredmet sp join op.predispitneObaveze po "+
            "where sp.studentIndeks = :studentIndeks and po.predmet = :predmet")
    Double getOstvareniPredispitniZaStudentaIzPredmeta(StudentIndeks studentIndeks, Predmet predmet);
    List<OstvareniPredispitni> findBySlusaPredmet(SlusaPredmet slusaPredmet);
}
