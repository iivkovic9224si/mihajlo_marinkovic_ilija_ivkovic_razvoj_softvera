package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramNewRequest;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.converters.StudProgramConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudProgramService {

    final StudijskiProgramRepository studijskiProgramRepository;
    //upisivanje novih entiteta
    public Long save(StudijskiProgramNewRequest request) {
        StudijskiProgram sp = StudProgramConverter.toStudijskiProgram(request);
        StudijskiProgram saved = studijskiProgramRepository.save(sp);
        return saved.getId();
    }
    //ispisuje sve studPrograme
    public List<StudijskiProgramResponse> findAll() {
        return new ArrayList<>(StudProgramConverter.toStudijskiProgramResponseList((List<StudijskiProgram>) studijskiProgramRepository.findAll()));
    }
    //ispisuje studProgram po ID
    public Optional<StudijskiProgramResponse> findById(Long id) {
        return studijskiProgramRepository.findById(id)
                .map(StudProgramConverter::toStudijskiProgramResponse);
    }
    public List<String> findAllOznake(){
        return new ArrayList<>(studijskiProgramRepository.findAllOznaka());
    }
    //brise studProgram sa ID
    public void deleteStudijskiProgram(Long id) {
        if (!studijskiProgramRepository.existsById(id)) {
            throw new RuntimeException("Studijski program sa ID " + id + " ne postoji.");
        }
        studijskiProgramRepository.deleteById(id);
    }
    public List<StudijskiProgramResponse> getAllSortedByGodinaDesc(){
        return new ArrayList<>(StudProgramConverter.toStudijskiProgramResponseList(studijskiProgramRepository.getAllSortedByGodinaDesc()));
    }

    // Update po ID
    public StudijskiProgramResponse update(Long id, StudijskiProgramNewRequest request) {
        StudijskiProgram updated = StudProgramConverter.toStudijskiProgram(request);
        updated.setId(id);

        Optional<StudijskiProgram> existing = studijskiProgramRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Studijski program sa ID " + id + " ne postoji.");
        }

        StudijskiProgram sp = existing.get();
        sp.setOznaka(updated.getOznaka());
        sp.setNaziv(updated.getNaziv());
        sp.setGodinaAkreditacije(updated.getGodinaAkreditacije());
        sp.setZvanje(updated.getZvanje());
        sp.setTrajanjeGodina(updated.getTrajanjeGodina());
        sp.setTrajanjeSemestara(updated.getTrajanjeSemestara());
        sp.setVrstaStudija(updated.getVrstaStudija());
        sp.setUkupnoEspb(updated.getUkupnoEspb());

        StudijskiProgram saved = studijskiProgramRepository.save(sp);
        return StudProgramConverter.toStudijskiProgramResponse(saved);
    }

    // Nalazi predmete po studijskom programu (iz requesta)
    public List<PredmetResponse> findPredmetByStudProgram(StudijskiProgramRequest request) {
        StudijskiProgram studProgram = StudProgramConverter.toStudijskiProgram(request);
        List<StudijskiProgram> spList = studijskiProgramRepository.findByOznaka(studProgram.getOznaka());

        if (spList.isEmpty()) {
            throw new RuntimeException("Studijski program sa oznakom " + studProgram.getOznaka() + " ne postoji!");
        }

        StudijskiProgram sp = spList.get(0);
        return studijskiProgramRepository.findPredmetByStudProgram(sp);
    }
}

