package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudProgramService {

    final StudijskiProgramRepository studijskiProgramRepository;
    //upisivanje novih entiteta
    public StudijskiProgram save(StudijskiProgram studijskiProgram) {
        return studijskiProgramRepository.save(studijskiProgram);
    }
    //ispisuje sve studPrograme
    public List<StudijskiProgramResponse> findAll() {
        return new ArrayList<>(Converters.toStudijskiProgramResponseList((List<StudijskiProgram>) studijskiProgramRepository.findAll()));
    }
    //ispisuje studProgram po ID
    public Optional<StudijskiProgramResponse> findById(Long id) {
        return studijskiProgramRepository.findById(id)
                .map(Converters::toStudijskiProgramResponse);
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
        return new ArrayList<>(Converters.toStudijskiProgramResponseList(studijskiProgramRepository.getAllSortedByGodinaDesc()));
    }

    public StudijskiProgram update(StudijskiProgram studijskiProgram) {
        Optional<StudijskiProgram> existing = studijskiProgramRepository.findById(studijskiProgram.getId());

        if (existing.isPresent()) {
            StudijskiProgram sp = existing.get();
            sp.setOznaka(studijskiProgram.getOznaka());
            sp.setNaziv(studijskiProgram.getNaziv());
            sp.setGodinaAkreditacije(studijskiProgram.getGodinaAkreditacije());
            sp.setZvanje(studijskiProgram.getZvanje());
            sp.setTrajanjeGodina(studijskiProgram.getTrajanjeGodina());
            sp.setTrajanjeSemestara(studijskiProgram.getTrajanjeSemestara());
            sp.setVrstaStudija(studijskiProgram.getVrstaStudija());
            sp.setUkupnoEspb(studijskiProgram.getUkupnoEspb());
            return studijskiProgramRepository.save(sp);
        } else {
            throw new RuntimeException("Studijski program sa ID " + studijskiProgram.getId() + " ne postoji");
        }
    }

    //nalazi predmete po oznaci studPrograma
    public List<PredmetResponse> findPredmetByStudProgram(StudijskiProgram studijskiProgram) {
        List<StudijskiProgram> spList = studijskiProgramRepository.findByOznaka(studijskiProgram.getOznaka());
        if (spList.isEmpty()) {
            throw new RuntimeException("Studijski program sa oznakom " + studijskiProgram.getOznaka() + " ne postoji!");
        }

        StudijskiProgram sp = spList.get(0); // uzmi najnoviji (ili onaj koji ti treba)
        return studijskiProgramRepository.findPredmetByStudProgram(sp);
    }

}

