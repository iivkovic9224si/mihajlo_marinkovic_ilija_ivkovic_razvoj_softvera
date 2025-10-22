package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudijskiTokAktivnostiRepository extends JpaRepository<org.raflab.studsluzba.model.StudijskiTokAktivnosti, Long> {

    //pregled svih upisanih godina za broj indeksa
    @Query("select tok.godina from StudijskiTokAktivnosti tok where tok.studentIndeks = :studentIndeks and tok.tip = 'UPIS'")
    List<Integer> getUpisaneGodineByStudentIndeks(StudentIndeks studentIndeks);

    //pregled obnovljenih godina za broj indeksa
    @Query("select tok.godina from StudijskiTokAktivnosti tok where tok.studentIndeks = :studentIndeks and tok.tip = 'OBNOVA'")
    List<Integer> getObnovljeneGodineByStudentIndeks(StudentIndeks studentIndeks);
}
