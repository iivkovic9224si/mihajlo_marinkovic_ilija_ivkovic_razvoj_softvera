package org.raflab.studsluzba.repositories;



import java.util.List;

import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPodaciRepository extends JpaRepository<StudentPodaci, Long> {	//	nasljedjene implementacije poput findById i findByAll

	@Query("select sp from StudentPodaci sp where "
			+ "(:ime is null or lower(sp.ime) like :ime) and "
			+ "(:prezime is null or lower(sp.prezime) like :prezime) and "
			+ "not exists (select indeks from StudentIndeks indeks where indeks.student = sp)")
	Page<StudentPodaci> findStudent(String ime, String prezime, Pageable pageable);

	@Query("select si from StudentIndeks si where si.aktivan=true and si.student.id = :studPodaciId")
	StudentIndeks getAktivanIndeks(Long studPodaciId);
	
	@Query("select si from StudentIndeks si where si.aktivan=false and si.student.id = :studPodaciId")
	List<StudentIndeks> getNeaktivniIndeksi(Long studPodaciId);

	//selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime ili oba da se unesu)
	@Query("select new org.raflab.studsluzba.controllers.response.StudentPodaciResponse(" +
			"sp.id, sp.ime, sp.prezime, sp.srednjeIme, sp.jmbg, sp.datumRodjenja, sp.mestoRodjenja, " +
			"sp.mestoPrebivalista, sp.drzavaRodjenja, sp.drzavljanstvo, sp.nacionalnost, sp.pol, sp.adresa, " +
			"sp.brojTelefonaMobilni, sp.brojTelefonaFiksni, sp.email, sp.brojLicneKarte, sp.licnuKartuIzdao, " +
			"sp.mestoStanovanja, sp.adresaStanovanja) " +
			"from StudentPodaci sp " +
			"where (:ime is null or lower(sp.ime) like :ime) " +
			"and (:prezime is null or lower(sp.prezime) like :prezime)")
	Page<StudentPodaciResponse> findStudentByImeandorPrezime(String ime, String prezime, Pageable pageable);
}
