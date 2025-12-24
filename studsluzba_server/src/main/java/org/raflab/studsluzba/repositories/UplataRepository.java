package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.Uplata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UplataRepository extends JpaRepository<Uplata, Long> {
    @Query("select sum(u.iznosUDinarima / u.srednjiKurs) from Uplata u where u.studentPodaci = :studentPodaci")
    Double uplaceno(StudentPodaci studentPodaci);

    List<Uplata> findByStudentPodaci(StudentPodaci studentPodaci);
}
