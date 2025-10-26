package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.VisokoSkolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoSkolskaUstanovaResponse;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.model.entity.VrstaVisokoSkolskihUstanova;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisokoSkolskaUstanovaService {

    private final VisokoSkolskaUstanovaRepository visokoSkolskaUstanovaRepository;

    //pravi nove VisokoSkolskeUstanove
    @Transactional
    public VisokoSkolskaUstanovaResponse save(VisokoSkolskaUstanovaRequest request) {
        // Proveri da li već postoji škola sa istim nazivom
        Optional<VisokoSkolskaUstanova> existing = visokoSkolskaUstanovaRepository.findByNaziv(request.getNaziv());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Visokoškolska ustanova sa ovim nazivom već postoji!");
        }
        VisokoSkolskaUstanova entity = Converters.toVisokoSkolskaUstanova(request);
        VisokoSkolskaUstanova saved = visokoSkolskaUstanovaRepository.save(entity);
        return Converters.toVisokoSkolskaUstanovaResponse(saved);
    }
    //nalazi sve visokoSkolskeUstanove
    public List<VisokoSkolskaUstanovaResponse> findAll() {
        List<VisokoSkolskaUstanova> list = (List<VisokoSkolskaUstanova>) visokoSkolskaUstanovaRepository.findAll();
        return Converters.toVisokoSkolskaUstanovaResponseList(list);
    }

    public VisokoSkolskaUstanovaResponse findByID(Long id) {
        return visokoSkolskaUstanovaRepository.findById(id)
                .map(Converters::toVisokoSkolskaUstanovaResponse).orElse(null);
    }

    @Transactional
    public VisokoSkolskaUstanovaResponse update(
            String stariNaziv, VrstaVisokoSkolskihUstanova staraVrsta,
            VisokoSkolskaUstanovaRequest noviPodaci) {

        List<VisokoSkolskaUstanova> visokoSkolskaUstanovaList = visokoSkolskaUstanovaRepository.findByNazivContainingIgnoreCaseAndVrsta(stariNaziv, staraVrsta);
        if (visokoSkolskaUstanovaList.isEmpty()) {
            throw new IllegalArgumentException("Visokoškolska ustanova sa starim nazivom i vrstom ne postoji!");
        }
        VisokoSkolskaUstanova visokoSkolskaUstanova = visokoSkolskaUstanovaList.get(0);
        List<VisokoSkolskaUstanova> duplicates = visokoSkolskaUstanovaRepository.findByNazivContainingIgnoreCaseAndVrsta(noviPodaci.getNaziv(), noviPodaci.getVrsta());
        boolean duplicateExists = duplicates.stream().anyMatch(e -> !e.equals(visokoSkolskaUstanova)); // ignorisi sebe
        if (duplicateExists) {
            throw new IllegalArgumentException("Već postoji visokoškolska ustanova sa novim nazivom i vrstom!");
        }

        visokoSkolskaUstanova.setNaziv(noviPodaci.getNaziv());
        visokoSkolskaUstanova.setVrsta(noviPodaci.getVrsta());
        VisokoSkolskaUstanova newVisokoSkolskaUstanova = visokoSkolskaUstanovaRepository.save(visokoSkolskaUstanova);
        return Converters.toVisokoSkolskaUstanovaResponse(newVisokoSkolskaUstanova);
    }


    //brise sa tim id-jem
    @Transactional
    public void delete(Long id) {
        VisokoSkolskaUstanova existing = visokoSkolskaUstanovaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Visokoškolska ustanova sa ID " + id + " ne postoji!"));
        visokoSkolskaUstanovaRepository.delete(existing);
    }
}

