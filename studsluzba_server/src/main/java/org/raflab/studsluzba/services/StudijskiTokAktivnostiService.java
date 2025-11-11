package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;
import org.raflab.studsluzba.repositories.StudijskiTokAktivnostiRepository;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudijskiTokAktivnostiService {
    final StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;

    public StudijskiTokAktivnosti save(StudijskiTokAktivnosti studijski){
        return studijskiTokAktivnostiRepository.save(studijski);
    }

    public List<StudijskiTokAktivnostiResponse> findAll(){
        return new ArrayList<>(StudentIndexAktivnostConverter.toStudijskiTokAktivnostiResponseList(studijskiTokAktivnostiRepository.findAll()));
    }

    public Optional<StudijskiTokAktivnostiResponse> findById(Long id){
        return studijskiTokAktivnostiRepository.findById(id).map(StudentIndexAktivnostConverter::toStudijskiTokAktivnostiResponse);
    }

    public StudijskiTokAktivnosti update(StudijskiTokAktivnosti studijski){
        Optional<StudijskiTokAktivnosti> existing = studijskiTokAktivnostiRepository.findById(studijski.getId());

        if(existing.isPresent()){
            StudijskiTokAktivnosti sta = existing.get();
            sta.setDatum(studijski.getDatum());
            sta.setTip(studijski.getTip());
            sta.setNapomena(studijski.getNapomena());
            sta.setGodina(studijski.getGodina());
            sta.setStudentIndeks(studijski.getStudentIndeks());
            return studijskiTokAktivnostiRepository.save(sta);
        } else{
            throw new RuntimeException("StudijskiTokAktivnosti " + studijski.getId() + " ne postoji.");
        }
    }
}
