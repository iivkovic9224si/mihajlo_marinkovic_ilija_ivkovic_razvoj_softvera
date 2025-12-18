package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.utils.converters.PolozenPredmetConverter;
import org.springframework.stereotype.Service;
import request.PolozenPredmetAwayRequest;
import request.PolozenPredmetHereRequest;
import response.PolozenPredmetResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolozenPredmetService {

    private final PolozenPredmetRepository polozenPredmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final PredmetRepository predmetRepository;
    private final VisokoSkolskaUstanovaRepository visokoSkolskaUstanovaRepository;

    // SAVE za AwayRequest
    public PolozenPredmetResponse saveAway(PolozenPredmetAwayRequest request) {
        StudentIndeks si = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(request.getIndeksGodina(), request.getIndeksBroj(), request.getIndeksStudProgramOznaka(), true)
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        VisokoSkolskaUstanova ustanova = visokoSkolskaUstanovaRepository.findBySifra(request.getVisokoSkolskaUstanovaSifra())
                .orElseThrow(() -> new RuntimeException("VisokoSkolskaUstanova ne postoji"));

        PolozenPredmet pp = PolozenPredmetConverter.fromAwayRequest(request, ustanova);
        pp.setStudentIndeks(si);
        pp.setPredmet(predmet);

        polozenPredmetRepository.save(pp);
        return PolozenPredmetConverter.toResponse(pp);
    }

    // SAVE za HereRequest
    public PolozenPredmetResponse saveHere(PolozenPredmetHereRequest request) {
        StudentIndeks si = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(request.getIndeksGodina(), request.getIndeksBroj(), request.getIndeksStudProgramOznaka(), true)
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        PolozenPredmet pp = PolozenPredmetConverter.fromHereRequest(request);
        pp.setStudentIndeks(si);
        pp.setPredmet(predmet);

        // ovde možeš dodati logiku za izlazakIspita po ID ako je potrebno
        // pp.setIzlazakIspita(...);

        polozenPredmetRepository.save(pp);
        return PolozenPredmetConverter.toResponse(pp);
    }

    // FIND by ID
    public PolozenPredmetResponse findById(Long id) {
        return polozenPredmetRepository.findById(id)
                .map(PolozenPredmetConverter::toResponse)
                .orElse(null);
    }

    // FIND all
    public List<PolozenPredmetResponse> findAll() {
        return polozenPredmetRepository.findAll()
                .stream()
                .map(PolozenPredmetConverter::toResponse)
                .collect(Collectors.toList());
    }

    // DELETE
    public void delete(Long id) {
        polozenPredmetRepository.deleteById(id);
    }

    //UPDATE
    public PolozenPredmetResponse updateHere(Long id, PolozenPredmetHereRequest request) {

        PolozenPredmet pp = polozenPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PolozenPredmet ne postoji"));

        StudentIndeks si = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(
                        request.getIndeksGodina(),
                        request.getIndeksBroj(),
                        request.getIndeksStudProgramOznaka(),
                        true
                )
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        pp.setStudentIndeks(si);
        pp.setPredmet(predmet);
        pp.setNapomena(request.getNapomena());

        polozenPredmetRepository.save(pp);
        return PolozenPredmetConverter.toResponse(pp);
    }

    public PolozenPredmetResponse updateAway(Long id, PolozenPredmetAwayRequest request) {

        PolozenPredmet pp = polozenPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PolozenPredmet ne postoji"));

        StudentIndeks si = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(
                        request.getIndeksGodina(),
                        request.getIndeksBroj(),
                        request.getIndeksStudProgramOznaka(),
                        true
                )
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        VisokoSkolskaUstanova ustanova = visokoSkolskaUstanovaRepository
                .findBySifra(request.getVisokoSkolskaUstanovaSifra())
                .orElseThrow(() -> new RuntimeException("VisokoSkolskaUstanova ne postoji"));

        pp.setStudentIndeks(si);
        pp.setPredmet(predmet);
        pp.setPriznatoOdInstitucije(ustanova);
        pp.setDatum(request.getDatum());
        pp.setBrojPoena(request.getBrojPoena());
        pp.setNapomena(request.getNapomena());
        pp.setPriznato(true);

        polozenPredmetRepository.save(pp);
        return PolozenPredmetConverter.toResponse(pp);
    }


}
