package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudProgramService {

    final StudijskiProgramRepository studijskiProgramRepository;

    public List<PredmetResponse> findPredmetByStudProgram(StudijskiProgram studijskiProgram) {
        // umesto da direktno koristiš prosleđeni objekat, pronađi pravi iz baze
        List<StudijskiProgram> spList = studijskiProgramRepository.findByOznaka(studijskiProgram.getOznaka());
        if (spList.isEmpty()) {
            throw new RuntimeException("Studijski program sa oznakom " + studijskiProgram.getOznaka() + " ne postoji!");
        }

        StudijskiProgram sp = spList.get(0); // uzmi najnoviji (ili onaj koji ti treba)
        return studijskiProgramRepository.findPredmetByStudProgram(sp);
    }
}

