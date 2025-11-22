package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends CrudRepository<SlusaPredmet, Long> {
	
	@Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :indeksId")
	List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(Long indeksId);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
			+ "and sp.drziPredmet.nastavnik.id = :idNastavnika  ")
	List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
	List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);

	@Query("select si from StudentIndeks si where not exists "
			+ "(select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet) ")
	List<StudentIndeks> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet);


	@Query("update SlusaPredmet sp set sp.drziPredmet.id = :drziPredmetId, " +
			"sp.skolskaGodina.id = :skolskaGodinaId " +
			"where sp.id = :id")
	void updateSlusaPredmet(Long id, Long skolskaGodinaId, Long drziPredmetId);

    // 🔹 DODATO: pronalazak SlusaPredmet po studentIndeksu i drziPredmetu
    @Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :studentIndeksId and sp.drziPredmet.id = :drziPredmetId")
    Optional<SlusaPredmet> findByStudentIndeksAndDrziPredmet(Long studentIndeksId, Long drziPredmetId);
}
