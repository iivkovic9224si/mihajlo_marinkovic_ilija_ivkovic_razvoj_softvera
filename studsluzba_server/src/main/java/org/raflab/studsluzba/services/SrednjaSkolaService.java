package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.entity.SrednjaSkola;
import org.raflab.studsluzba.model.entity.VrstaSrednjeSkole;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;
import org.raflab.studsluzba.utils.converters.SrednjaSkolaConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SrednjaSkolaService {

    private final SrednjaSkolaRepository srednjaSkolaRepository;

    // ----------------- CREATE -----------------
    public SrednjaSkolaResponse save(SrednjaSkolaRequest request) {
        Optional<SrednjaSkola> existing = srednjaSkolaRepository.findByNazivAndVrstaSrednjeSkole(
                request.getNaziv(), request.getVrstaSrednjeSkole()
        );
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Srednja škola sa nazivom '" + request.getNaziv() +
                    "' i vrstom '" + request.getVrstaSrednjeSkole() + "' već postoji!");
        }
        SrednjaSkola skola = SrednjaSkolaConverter.toSrednjaSkola(request);
        SrednjaSkola saved = srednjaSkolaRepository.save(skola);
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(saved);
    }

    // ----------------- READ ALL -----------------
    public List<SrednjaSkolaResponse> findAll() {
        return SrednjaSkolaConverter.toSrednjaSkolaResponseList(srednjaSkolaRepository.findAll());
    }

    // ----------------- READ BY ID -----------------
    public SrednjaSkolaResponse findById(Long id) {
        SrednjaSkola skola = srednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Srednja škola sa ID " + id + " ne postoji!"));
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(skola);
    }

    // ----------------- READ BY NAZIV + VRSTA -----------------
    public SrednjaSkolaResponse findByNazivAndVrsta(String naziv, VrstaSrednjeSkole vrsta) {
        SrednjaSkola skola = srednjaSkolaRepository.findByNazivAndVrstaSrednjeSkole(naziv, vrsta)
                .orElseThrow(() -> new IllegalArgumentException("Srednja škola sa nazivom '" + naziv +
                        "' i vrstom '" + vrsta + "' ne postoji!"));
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(skola);
    }

    // ----------------- UPDATE -----------------
    public SrednjaSkolaResponse update(String stariNaziv, VrstaSrednjeSkole staraVrsta, SrednjaSkolaRequest noviPodaci) {
        SrednjaSkola skola = srednjaSkolaRepository.findByNazivAndVrstaSrednjeSkole(
                stariNaziv, staraVrsta
        ).orElseThrow(() -> new IllegalArgumentException("Srednja škola sa nazivom '" + stariNaziv +
                "' i vrstom '" + staraVrsta + "' ne postoji!"));

        // Update polja na nova
        skola.setNaziv(noviPodaci.getNaziv());
        skola.setVrstaSrednjeSkole(noviPodaci.getVrstaSrednjeSkole());

        SrednjaSkola updated = srednjaSkolaRepository.save(skola);
        return SrednjaSkolaConverter.toSrednjaSkolaResponse(updated);
    }


    // ----------------- DELETE -----------------
    public void delete(Long id) {
        if (!srednjaSkolaRepository.existsById(id)) {
            throw new IllegalArgumentException("Srednja škola sa ID " + id + " ne postoji!");
        }
        srednjaSkolaRepository.deleteById(id);
    }
}
