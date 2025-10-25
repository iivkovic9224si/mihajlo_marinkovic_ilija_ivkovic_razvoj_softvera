package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiTokAktivnostiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;

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

    @Transactional(readOnly = true)
    public int findBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.
                findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);

        return findNextAvailableNumber(brojeviList);
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

    public List<PredmetResponse> findPolozeniPredmetiByStudentIndeks(StudentIndeks studentIndeks) {
        return polozenPredmetRepository.findPolozeniPredmetiByStudentIndeks(studentIndeks);
    }

    public List<Integer> getUpisaneGodineByStudentIndeks(StudentIndeks studentIndeks) {
        return studijskiTokAktivnostiRepository.getUpisaneGodineByStudentIndeks(studentIndeks);
    }

    public List<Integer> getObnovljeneGodineByStudentIndeks(StudentIndeks studentIndeks) {
        return studijskiTokAktivnostiRepository.getObnovljeneGodineByStudentIndeks(studentIndeks);
    }

    public Page<StudentPodaciResponse> getStudentPodaciByImeandorPrezime(String ime, String prezime, Pageable pageable) {
        return studentPodaciRepository.findStudentByImeandorPrezime(ime, prezime, pageable);
    }
}
