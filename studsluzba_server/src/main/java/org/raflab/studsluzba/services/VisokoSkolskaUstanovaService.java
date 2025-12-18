package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;
import org.raflab.studsluzba.utils.converters.VisokoSkolskaUstanovaConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.VisokoSkolskaUstanovaRequest;
import response.VisokoSkolskaUstanovaResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisokoSkolskaUstanovaService {

    private final VisokoSkolskaUstanovaRepository repository;

    // ----------------- CREATE -----------------
    @Transactional
    public VisokoSkolskaUstanovaResponse save(VisokoSkolskaUstanovaRequest request) {
        if(request.getSifra() == null || request.getSifra().isBlank()) {
            throw new IllegalArgumentException("Šifra ustanove je obavezna i ne sme biti prazna!");
        }

        if(repository.findBySifra(request.getSifra()).isPresent()) {
            throw new IllegalArgumentException("Ustanova sa šifrom '" + request.getSifra() + "' već postoji!");
        }

        VisokoSkolskaUstanova ustanova = VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanova(request);
        VisokoSkolskaUstanova saved = repository.save(ustanova);
        return VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponse(saved);
    }

    // ----------------- READ ALL -----------------
    public List<VisokoSkolskaUstanovaResponse> findAll() {
        return VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponseList((List<VisokoSkolskaUstanova>) repository.findAll());
    }

    // ----------------- READ BY SIFRA -----------------
    public VisokoSkolskaUstanovaResponse findBySifra(String sifra) {
        if(sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra ustanove ne sme biti prazna!");
        }

        VisokoSkolskaUstanova ustanova = repository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Ustanova sa šifrom '" + sifra + "' ne postoji!"));

        return VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponse(ustanova);
    }

    // ----------------- UPDATE -----------------
    @Transactional
    public VisokoSkolskaUstanovaResponse update(String sifra, VisokoSkolskaUstanovaRequest noviPodaci) {
        if(sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra ustanove ne sme biti prazna!");
        }

        VisokoSkolskaUstanova ustanova = repository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Ustanova sa šifrom '" + sifra + "' ne postoji!"));

        // Update polja
        ustanova.setNaziv(noviPodaci.getNaziv());
        ustanova.setVrsta(noviPodaci.getVrsta());
        ustanova.setSifra(noviPodaci.getSifra() != null ? noviPodaci.getSifra() : sifra);

        // Ako menjamo šifru, proveri da nova ne postoji
        if(!sifra.equals(ustanova.getSifra()) && repository.findBySifra(ustanova.getSifra()).isPresent()) {
            throw new IllegalArgumentException("Nova šifra '" + ustanova.getSifra() + "' već postoji!");
        }

        VisokoSkolskaUstanova updated = repository.save(ustanova);
        return VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponse(updated);
    }

    // ----------------- DELETE -----------------
    @Transactional
    public void delete(String sifra) {
        if(sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra ustanove ne sme biti prazna!");
        }

        VisokoSkolskaUstanova ustanova = repository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Ustanova sa šifrom '" + sifra + "' ne postoji!"));

        repository.delete(ustanova);
    }
}
