package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.converters.PredmetConverter;
import org.raflab.studsluzba.utils.converters.StudentIndeksConverter;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostConverter;
import org.raflab.studsluzba.utils.converters.StudentPodaciConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentIndeksService {
    
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private PolozenPredmetRepository polozenPredmetRepository;
    @Autowired
    private StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private SrednjaSkolaRepository srednjaSkolaRepository;
    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;

    @Transactional(readOnly = true)
    public int findBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.
                findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);

        return findNextAvailableNumber(brojeviList);
    }

    public StudentIndeks save(StudentIndeksRequest siRequest) {
        StudentIndeks si = StudentIndeksConverter.toStudentIndeks(siRequest);
        return studentIndeksRepository.save(si);
    }

    @Transactional
    public Long saveIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = StudentIndeksConverter.toStudentIndeks(studentIndeksRequest);

        int nextBroj = findBroj(studentIndeksRequest.getGodina(), studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setBroj(nextBroj);

        StudentPodaci studentPodaci = studentPodaciRepository.findById(studentIndeksRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student sa ID " + studentIndeksRequest.getStudentId() + " nije pronađen"));
        studentIndeks.setStudent(studentPodaci);

        List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(studentIndeksRequest.getStudProgramOznaka());
        if (studijskiProgrami.isEmpty()) {
            throw new RuntimeException("No StudijskiProgram found with oznaka: " + studentIndeksRequest.getStudProgramOznaka());
        }
        studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));

        try {
            StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
            return savedStudentIndeks.getId();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the StudentIndeks.", e);
        }
    }

    public Long update(Long id, StudentIndeksRequest siRequest) {
        StudentIndeks studentIndeks = studentIndeksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentIndeks sa ID " + id + " ne postoji"));

        studentIndeks.setGodina(siRequest.getGodina());
        studentIndeks.setAktivan(siRequest.isAktivan());
        studentIndeks.setNacinFinansiranja(siRequest.getNacinFinansiranja());
        studentIndeks.setVaziOd(siRequest.getVaziOd());

        StudentIndeks saved = studentIndeksRepository.save(studentIndeks);
        return saved.getId();
    }

    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) return 1;

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList()); // <= works on JDK 8+

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) return expected;
            expected++;
        }
        return expected;
    }

    public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
        return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }

    public Page<PredmetResponse> findPolozeniPredmetiByStudentIndeks(StudentIndeksRequest studentIndeksRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
                studentIndeksRequest.getStudProgramOznaka(),
                studentIndeksRequest.getGodina(),
                studentIndeksRequest.getBroj()
        );

        if (studentIndeks == null) {
            throw new RuntimeException("Student indeks nije pronađen u bazi.");
        }

        Page<Predmet> pageResult = polozenPredmetRepository.findPolozeniPredmetiByStudentIndeks(studentIndeks, pageable);
        return pageResult.map(PredmetConverter::toPredmetResponse);
    }

    public Page<PredmetResponse> findNePolozeniPredmetiByStudentIndeks(StudentIndeksRequest studentIndeksRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
                studentIndeksRequest.getStudProgramOznaka(),
                studentIndeksRequest.getGodina(),
                studentIndeksRequest.getBroj()
        );

        if (studentIndeks == null) {
            throw new RuntimeException("Student indeks nije pronađen u bazi.");
        }

        Page<Predmet> pageResult = polozenPredmetRepository.findNePolozeniPredmetiByStudentIndeks(studentIndeks, pageable);
        return pageResult.map(PredmetConverter::toPredmetResponse);
    }

    public List<Integer> getUpisaneGodineByStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
                studentIndeksRequest.getStudProgramOznaka(),
                studentIndeksRequest.getGodina(),
                studentIndeksRequest.getBroj()
        );

        if (studentIndeks == null) {
            throw new RuntimeException("Student indeks nije pronađen u bazi.");
        }

        return studijskiTokAktivnostiRepository.getUpisaneGodineByStudentIndeks(studentIndeks);
    }

    public Page<StudentPodaciResponse> getStudentPodaciByImeandorPrezime(String ime, String prezime, Pageable pageable) {
        return StudentPodaciConverter.toStudentPodaciResponse(studentPodaciRepository.findStudentByImeandorPrezime(ime, prezime, pageable));
    }

    public StudentPodaciResponse getStudentPodaciByStudentIndeks(String oznakaPrograma, int broj, int godina) {
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(oznakaPrograma, broj, godina);
        if (si == null) {
            throw new RuntimeException("Student indeks nije pronađen u bazi.");
        }
        return StudentPodaciConverter.toStudentPodaciResponse(studentPodaciRepository.getStudentPodaciFromStudentIndeks(si));
    }

    public List<StudentPodaciResponse> getStudentPodaciBySrednjaSkola(String sifra){
        SrednjaSkola srednjaSkola = srednjaSkolaRepository.findBySifra(sifra)
                .orElseThrow(() -> new RuntimeException("Srednja skola pod sifrom " + sifra + " nije pronadjena"));

        return StudentPodaciConverter.toStudentPodaciResponseList(studentPodaciRepository.getStudentiBySrednjaSkola(srednjaSkola));
    }

    public List<Integer> getObnovljeneGodine(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
                studentIndeksRequest.getStudProgramOznaka(),
                studentIndeksRequest.getGodina(),
                studentIndeksRequest.getBroj()
        );

        if (studentIndeks == null) {
            throw new RuntimeException("Student indeks nije pronađen u bazi.");
        }

        return studijskiTokAktivnostiRepository.getObnovljeneGodineByStudentIndeks(studentIndeks);
    }

    @Transactional
    public void deleteByStudentIndeks(String studProgramOznaka, int broj, int godina) {
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(studProgramOznaka, broj, godina);

        if (studentIndeks == null) {
            throw new RuntimeException("Student indeks (" + studProgramOznaka + "-" + broj + "/" + godina + ") nije pronađen.");
        }

        try {
            studentIndeksRepository.delete(studentIndeks);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Brisanje nije moguće – indeks je povezan sa drugim podacima (predmeti, aktivnosti, podaci o studentu).", e);
        }
    }
    //novo
    @Transactional(readOnly = true)
    public List<StudentIndeksResponse> getAllIndeksiForStudentPodaci(Long studentPodaciId) {
        List<StudentIndeks> indeksi = studentIndeksRepository.findStudentIndeksiForStudentPodaciId(studentPodaciId);
        return indeksi.stream()
                .map(StudentIndexAktivnostConverter::toStudentIndeksResponse)
                .collect(Collectors.toList());
    }

}
