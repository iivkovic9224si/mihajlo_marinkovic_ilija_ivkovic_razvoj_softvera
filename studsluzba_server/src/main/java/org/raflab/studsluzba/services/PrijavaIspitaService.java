package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.PrijavaIspita;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.converters.PrijavaIspitaConverter;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrijavaIspitaService {

    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final IspitRepository ispitRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;

    // ------------------ SAVE ------------------
    public PrijavaIspitaResponse save(PrijavaIspitaRequest prijavaIspitaRequest) {
        Ispit ispit = ispitRepository.findById(prijavaIspitaRequest.getId())
                .orElseThrow(() -> new RuntimeException("Ispit nije pronađen"));

        StudentIndeks studentIndeks = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznaka(
                        prijavaIspitaRequest.getIndeksGodina(),
                        prijavaIspitaRequest.getIndeksBroj(),
                        prijavaIspitaRequest.getIndeksStudProgramOznaka())
                .orElseThrow(() -> new RuntimeException("Student indeks nije pronađen"));

        SlusaPredmet slusaPredmet = slusaPredmetRepository
                .findByStudentIndeksAndDrziPredmet(studentIndeks.getId(), ispit.getDrziPredmet().getId())
                .orElseThrow(() -> new RuntimeException("SlusaPredmet nije pronađen"));

        PrijavaIspita prijavaIspita = PrijavaIspitaConverter
                .toPrijavaIspita(prijavaIspitaRequest, ispit, slusaPredmet);

        prijavaIspitaRepository.save(prijavaIspita);
        return PrijavaIspitaConverter.toPrijavaIspitaResponse(prijavaIspita);
    }

    // ------------------ FIND ALL ------------------
    public List<PrijavaIspitaResponse> findAll() {
        List<PrijavaIspita> allPrijave = (List<PrijavaIspita>) prijavaIspitaRepository.findAll();
        List<PrijavaIspitaResponse> responses = new ArrayList<>();
        for (PrijavaIspita prijava : allPrijave) {
            responses.add(PrijavaIspitaConverter.toPrijavaIspitaResponse(prijava));
        }
        return responses;
    }

    // ------------------ FIND BY ID ------------------
    public PrijavaIspitaResponse findById(Long id) {
        PrijavaIspita prijavaIspita = prijavaIspitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prijava ispita nije pronađena za ID: " + id));
        return PrijavaIspitaConverter.toPrijavaIspitaResponse(prijavaIspita);
    }

    // ------------------ DELETE ------------------
    public void delete(Long id) {
        PrijavaIspita prijavaIspita = prijavaIspitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prijava ispita nije pronađena za ID: " + id));
        prijavaIspitaRepository.delete(prijavaIspita);
    }

    //NALAZI SVE PRIJAVLJENE STUDENTE ZA ISPIT
    public List<StudentIndeksResponse> findStudentiByIspitId(Long ispitId) {
        List<SlusaPredmet> slusaPredmeti =
                prijavaIspitaRepository.findSlusaPredmetByIspitId(ispitId);

        List<StudentIndeksResponse> responses = new ArrayList<>();
        for (SlusaPredmet sp : slusaPredmeti) {
            if (sp.getStudentIndeks() != null) {
                StudentIndeks studenti = sp.getStudentIndeks();
                responses.add(StudentIndexAktivnostConverter.toStudentIndeksResponseWithoutPodaci(studenti));
            }
        }
        return responses;
    }

    // ------------------ UPDATE ------------------
    public PrijavaIspitaResponse update(Long id, PrijavaIspitaRequest request) {

        PrijavaIspita prijava = prijavaIspitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prijava ispita ne postoji sa ID: " + id));

        Ispit ispit = ispitRepository.findById(request.getIspitID())
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        StudentIndeks studentIndeks = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznaka(
                        request.getIndeksGodina(),
                        request.getIndeksBroj(),
                        request.getIndeksStudProgramOznaka())
                .orElseThrow(() -> new RuntimeException("Student indeks nije pronađen"));

        SlusaPredmet slusaPredmet = slusaPredmetRepository
                .findByStudentIndeksAndDrziPredmet(
                        studentIndeks.getId(),
                        ispit.getDrziPredmet().getId()
                )
                .orElseThrow(() -> new RuntimeException("SlusaPredmet nije pronađen"));

        prijava.setDatumPrijave(request.getDatumPrijave());
        prijava.setIspit(ispit);
        prijava.setSlusaPredmet(slusaPredmet);

        prijavaIspitaRepository.save(prijava);

        return PrijavaIspitaConverter.toPrijavaIspitaResponse(prijava);
    }

}
