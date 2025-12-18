package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentIndeksRepository extends JpaRepository<StudentIndeks, Long> {


	@Query("select indeks from StudentIndeks indeks where indeks.studProgramOznaka = ?1 and indeks.godina = ?2 "
			+ "and indeks.broj = ?3 ")
    StudentIndeks findStudentIndeks(String studProgramOznaka, int godina, int broj);

    @Query("select si from StudentIndeks si " +
            "join fetch si.student s " +   // ovo će odmah učitati student entitet
            "where si.studProgramOznaka = :studProgramOznaka " +
            "and si.godina = :godina " +
            "and si.broj = :broj")
    StudentIndeks findStudentIndeksWithStudent(@Param("studProgramOznaka") String studProgramOznaka,
                                               @Param("godina") int godina,
                                               @Param("broj") int broj);


	//TODO dodati da se gledaju samo aktivni indeksi
	@Query("select indeks from StudentIndeks indeks where "
			+ "(:ime is null or lower(indeks.student.ime) like :ime) and "
			+ "(:prezime is null or lower(indeks.student.prezime) like :prezime) and "
			+ "(:studProgramOznaka is null or lower(indeks.studProgramOznaka) like :studProgramOznaka) and"
			+ "(:godina is null or indeks.godina = :godina) and "
			+ "(:broj is null or indeks.broj = :broj)")
	Page<StudentIndeks> findStudentIndeks(String ime, String prezime, String studProgramOznaka, Integer godina, Integer broj, Pageable pageable);

	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci")
	List<StudentIndeks> findStudentIndeksiForStudentPodaciId(Long idStudentPodaci);

	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci and si.aktivan = true")
	StudentIndeks findAktivanStudentIndeksiByStudentPodaciId(Long idStudentPodaci);

	@Query("SELECT s.broj FROM StudentIndeks s WHERE s.godina = :godina AND s.studProgramOznaka = :studProgramOznaka AND s.aktivan = true ORDER BY s.broj ASC")
	List<Integer> findBrojeviByGodinaAndStudProgramOznaka(@Param("godina") int godina, @Param("studProgramOznaka") String studProgramOznaka);

	Optional<StudentIndeks> findByGodinaAndBrojAndStudProgramOznakaAndAktivan(int godina, int broj, String studProgramOznaka, boolean aktivan);

    Optional<StudentIndeks> findByGodinaAndBrojAndStudProgramOznaka(int godina, int broj, String studProgramOznaka);


    @Query("select si from StudentIndeks si join si.studijskiProgram sp where sp.oznaka = :studProgramOznaka and si.broj = :broj and si.godina = :godina")
	StudentIndeks getStudentIndeks(@Param("studProgramOznaka") String studProgramOznaka, @Param("broj") int broj, @Param("godina") int godina);
}
