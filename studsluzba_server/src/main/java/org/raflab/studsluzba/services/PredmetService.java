package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.converters.PredmetConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredmetService {
    final PredmetRepository predmetRepository;
    final StudijskiProgramRepository studijskiProgramRepository;

    // SAVE
    public PredmetResponse save(PredmetRequest request) {
        if(request.getOznakaStudProgram() == null || request.getOznakaStudProgram().isBlank()) {
            throw new IllegalArgumentException("Oznaka studijskog programa ne sme biti prazna.");
        }

        StudijskiProgram studProgram = studijskiProgramRepository.findByOznakaSingle(request.getOznakaStudProgram());
        if (studProgram == null) {
            throw new IllegalArgumentException(
                    "Studijski program sa oznakom '" + request.getOznakaStudProgram() + "' ne postoji!"
            );
        }

        Predmet predmet = PredmetConverter.toPredmet(request, studProgram);
        Predmet saved = predmetRepository.save(predmet);
        return PredmetConverter.toPredmetResponse(saved);
    }

    // FIND ALL
    public List<PredmetResponse> findAll() {
        List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAll();
        if(predmeti.isEmpty()) {
            throw new IllegalArgumentException("Trenutno nema predmeta u bazi.");
        }
        return PredmetConverter.toPredmetResponseList(predmeti);
    }

    // FIND BY ID
    public PredmetResponse findById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("ID predmeta ne sme biti null.");
        }
        return predmetRepository.findById(id)
                .map(PredmetConverter::toPredmetResponse)
                .orElseThrow(() -> new IllegalArgumentException("Predmet sa ID " + id + " ne postoji."));
    }

    // UPDATE
    public PredmetResponse update(String sifra, PredmetRequest request) {
        if(sifra == null || sifra.isBlank()) {
            throw new IllegalArgumentException("Šifra predmeta ne sme biti prazna.");
        }

        Predmet predmet = predmetRepository.findBySifra(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Predmet sa šifrom '" + sifra + "' ne postoji!"));

        if(request.getOznakaStudProgram() == null || request.getOznakaStudProgram().isBlank()) {
            throw new IllegalArgumentException("Oznaka studijskog programa ne sme biti prazna.");
        }

        StudijskiProgram studProgram = studijskiProgramRepository.findByOznakaSingle(request.getOznakaStudProgram());
        if (studProgram == null) {
            throw new IllegalArgumentException(
                    "Studijski program sa oznakom '" + request.getOznakaStudProgram() + "' ne postoji!"
            );
        }

        predmet.setNaziv(request.getNaziv());
        predmet.setSifra(request.getSifra());
        predmet.setEspb(request.getEspb());
        predmet.setOpis(request.getOpis());
        predmet.setObavezan(request.isObavezan());
        predmet.setStudProgram(studProgram);

        Predmet updated = predmetRepository.save(predmet);
        return PredmetConverter.toPredmetResponse(updated);
    }

    // DELETE
    public void delete(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("ID predmeta ne sme biti null.");
        }
        if (!predmetRepository.existsById(id)) {
            throw new IllegalArgumentException("Predmet sa ID " + id + " ne postoji!");
        }
        predmetRepository.deleteById(id);
    }

    // FIND BY GODINA AKREDITACIJE
    public List<PredmetResponse> findByGodinaAkreditacije(Integer godinaAkreditacije) {
        if(godinaAkreditacije == null || godinaAkreditacije <= 0) {
            throw new IllegalArgumentException("Godina akreditacije mora biti veća od 0.");
        }

        List<Predmet> predmeti = predmetRepository.getPredmetForGodinaAkreditacije(godinaAkreditacije);
        if(predmeti.isEmpty()) {
            throw new IllegalArgumentException("Nema predmeta za godinu akreditacije " + godinaAkreditacije);
        }

        return PredmetConverter.toPredmetResponseList(predmeti);
    }

    // FIND BY STUDPROGRAM AND OBAVEZAN
    public List<PredmetResponse> findByStudProgramAndObavezan(String oznakaStudProgram, boolean obavezan) {
        if(oznakaStudProgram == null || oznakaStudProgram.isBlank()) {
            throw new IllegalArgumentException("Oznaka studijskog programa ne sme biti prazna.");
        }

        StudijskiProgram studProgram = studijskiProgramRepository.findByOznakaSingle(oznakaStudProgram);
        if (studProgram == null) {
            throw new IllegalArgumentException(
                    "Studijski program sa oznakom '" + oznakaStudProgram + "' ne postoji!"
            );
        }

        List<Predmet> predmeti = predmetRepository.getPredmetsByStudProgramAndObavezan(studProgram, obavezan);
        if(predmeti.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nema " + (obavezan ? "obaveznih" : "izbornih") + " predmeta za studijski program '" + oznakaStudProgram + "'."
            );
        }

        return PredmetConverter.toPredmetResponseList(predmeti);
    }
}
