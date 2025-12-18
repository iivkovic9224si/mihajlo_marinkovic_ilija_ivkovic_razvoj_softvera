package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.SrednjaSkola;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;
import org.raflab.studsluzba.utils.converters.SrednjaSkolaConverter;
import org.springframework.stereotype.Service;
import request.SrednjaSkolaRequest;
import response.SrednjaSkolaResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SrednjaSkolaService {

    private final SrednjaSkolaRepository srednjaSkolaRepository;

    // ----------------- CREATE -----------------
    public SrednjaSkolaResponse save(SrednjaSkolaRequest request) {
        if (request.getSifra() == null || request.getSifra().isBlank()) {
            throw new IllegalArgumentException("Šifra škole je obavezna i ne sme biti prazna!");
        }

        if (srednjaSkolaRepository.findBySifra(request.getSifra()).isPresent()) {
            throw new IllegalArgumentException("Srednja škola sa šifrom '" + request.getSifra() + "' već postoji!");
        }

        SrednjaSkola skola = SrednjaSkolaConverter.toSrednjaSkola(request);
        SrednjaSkola saved = srednjaSkolaRepository.save(skola);
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(saved);
    }

    // ----------------- READ ALL -----------------
    public List<SrednjaSkolaResponse> findAll() {
        return SrednjaSkolaConverter.toSrednjaSkolaResponseList(srednjaSkolaRepository.findAll());
    }

    // ----------------- READ BY SIFRA -----------------
    public SrednjaSkolaResponse findBySifra(String sifra) {
        if (sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra škole ne sme biti prazna!");
        }

        SrednjaSkola skola = srednjaSkolaRepository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Srednja škola sa šifrom '" + sifra + "' ne postoji!"));
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(skola);
    }

    // ----------------- UPDATE -----------------
    public SrednjaSkolaResponse update(String sifra, SrednjaSkolaRequest noviPodaci) {
        if (sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra škole ne sme biti prazna!");
        }

        SrednjaSkola skola = srednjaSkolaRepository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Srednja škola sa šifrom '" + sifra + "' ne postoji!"));

        // Update samo dozvoljenih polja
        skola.setNaziv(noviPodaci.getNaziv());
        skola.setVrstaSrednjeSkole(noviPodaci.getVrstaSrednjeSkole());
        skola.setSifra(noviPodaci.getSifra() != null ? noviPodaci.getSifra() : sifra);

        // Ako menjamo šifru, proveri da nova ne postoji već u bazi
        if (!sifra.equals(skola.getSifra()) && srednjaSkolaRepository.findBySifra(skola.getSifra()).isPresent()) {
            throw new IllegalArgumentException("Nova šifra '" + skola.getSifra() + "' već postoji!");
        }

        SrednjaSkola updated = srednjaSkolaRepository.save(skola);
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(updated);
    }

    // ----------------- DELETE -----------------
    public void delete(String sifra) {
        if (sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra škole ne sme biti prazna!");
        }

        SrednjaSkola skola = srednjaSkolaRepository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Srednja škola sa šifrom '" + sifra + "' ne postoji!"));

        srednjaSkolaRepository.delete(skola);
    }
}
