package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.Grupa;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.GrupaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.converters.GrupaConverter;
import org.springframework.stereotype.Service;
import request.GrupaRequest;
import response.GrupaResponse;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupaService {

    private final GrupaRepository grupaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final PredmetRepository predmetRepository;

    // CREATE
    public GrupaResponse save(GrupaRequest request) {
        StudijskiProgram sp = studijskiProgramRepository.findByOznakaSingle(request.getStudijskiProgramOznaka());
        if (sp == null) throw new IllegalArgumentException("Studijski program ne postoji!");

        List<Predmet> predmeti = new ArrayList<>();
        for (String sifra : request.getSifrePredmeta()) {
            Predmet p = predmetRepository.findBySifra(sifra)
                    .orElseThrow(() -> new IllegalArgumentException("Predmet sa šifrom " + sifra + " ne postoji!"));
            predmeti.add(p);
        }

        Grupa grupa = new Grupa();
        grupa.setStudijskiProgram(sp);
        grupa.setPredmeti(predmeti);

        Grupa saved = grupaRepository.save(grupa);
        return GrupaConverter.toGrupaResponse(saved);
    }

    // FIND ALL
    @Transactional
    public List<GrupaResponse> findAll() {
        List<Grupa> grupe = (List<Grupa>) grupaRepository.findAll();
        if (grupe.isEmpty()) throw new IllegalArgumentException("Nema grupa u bazi.");
        return GrupaConverter.toGrupaResponseList(grupe);
    }

    // FIND BY ID
    public GrupaResponse findById(Long id) {
        Grupa grupa = grupaRepository.findByIdWithPredmeti(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupa sa ID " + id + " ne postoji."));
        return GrupaConverter.toGrupaResponse(grupa);
    }

    // UPDATE
    public GrupaResponse update(Long id, GrupaRequest request) {
        Grupa grupa = grupaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupa sa ID " + id + " ne postoji."));

        StudijskiProgram sp = studijskiProgramRepository.findByOznakaSingle(request.getStudijskiProgramOznaka());
        if (sp == null) throw new IllegalArgumentException("Studijski program ne postoji!");

        List<Predmet> predmeti = new ArrayList<>();
        for (String sifra : request.getSifrePredmeta()) {
            Predmet p = predmetRepository.findBySifra(sifra)
                    .orElseThrow(() -> new IllegalArgumentException("Predmet sa šifrom " + sifra + " ne postoji!"));
            predmeti.add(p);
        }

        grupa.setStudijskiProgram(sp);
        grupa.setPredmeti(predmeti);

        Grupa updated = grupaRepository.save(grupa);
        return GrupaConverter.toGrupaResponse(updated);
    }

    // DELETE
    public void delete(Long id) {
        if (!grupaRepository.existsById(id))
            throw new IllegalArgumentException("Grupa sa ID " + id + " ne postoji.");
        grupaRepository.deleteById(id);
    }
}
