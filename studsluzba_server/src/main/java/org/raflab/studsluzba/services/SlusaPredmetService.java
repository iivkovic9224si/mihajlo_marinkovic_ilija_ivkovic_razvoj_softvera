package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.converters.SlusaPredmetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SlusaPredmetService {
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;

    @Transactional
    public SlusaPredmetResponse save(SlusaPredmetRequest slusaPredmetRequest) {
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByGodina(slusaPredmetRequest.getSkolskaGodina());
        if(skolskaGodina == null) {
            throw new IllegalArgumentException("Skolska godina " + slusaPredmetRequest.getSkolskaGodina() + " ne postoji.");
        }
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(slusaPredmetRequest.getStudProgramOznaka(), slusaPredmetRequest.getStudentGodina(), slusaPredmetRequest.getStudentBroj());
        if(studentIndeks == null) {
            throw new IllegalArgumentException("Student sa indeksom "+ slusaPredmetRequest.getStudProgramOznaka() + slusaPredmetRequest.getStudentBroj() + slusaPredmetRequest.getStudentGodina() +" ne postoji.");
        }
        DrziPredmet drziPredmet = drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra(slusaPredmetRequest.getNastavnikMail(), slusaPredmetRequest.getSifraPredmeta());
        if(drziPredmet == null) {
            throw new IllegalArgumentException("Nastavnik sa mejlom " + slusaPredmetRequest.getNastavnikMail() + " ne drzi predmet sa sifrom " + slusaPredmetRequest.getSifraPredmeta());
        }

        SlusaPredmet slusaPredmet = SlusaPredmetConverter.toSlusaPredmet(skolskaGodina, studentIndeks, drziPredmet);
        SlusaPredmet saved = slusaPredmetRepository.save(slusaPredmet);
        return SlusaPredmetConverter.toSlusaPredmetResponse(saved);
    }

    @Transactional
    public void update(SlusaPredmetRequest slusaPredmetRequest) {
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByGodina(slusaPredmetRequest.getSkolskaGodina());
        if(skolskaGodina == null) {
            throw new IllegalArgumentException("Skolska godina " + slusaPredmetRequest.getSkolskaGodina() + " ne postoji.");
        }
        long godinaId = skolskaGodina.getId();
        DrziPredmet drziPredmet = drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra(slusaPredmetRequest.getNastavnikMail(), slusaPredmetRequest.getSifraPredmeta());
        if(drziPredmet == null) {
            throw new IllegalArgumentException("Nastavnik sa mejlom " + slusaPredmetRequest.getNastavnikMail() + " ne drzi predmet sa sifrom " + slusaPredmetRequest.getSifraPredmeta());
        }
        long drziPredmetId = drziPredmet.getId();

        slusaPredmetRepository.updateSlusaPredmet(slusaPredmetRequest.getId(), godinaId, drziPredmetId);
    }

    public void delete(SlusaPredmetRequest slusaPredmetRequest) {
        slusaPredmetRepository.deleteById(slusaPredmetRequest.getId());
    }

    public Optional<SlusaPredmetResponse> findById(long id) {
        return slusaPredmetRepository.findById(id).map(SlusaPredmetConverter::toSlusaPredmetResponse);
    }


    public List<SlusaPredmetResponse> getSlusaPredmetiZaStudenta(String studProgramOznaka, int studentGodina, int studentBroj) {
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(studProgramOznaka, studentGodina, studentBroj);
        if (studentIndeks == null) {
            throw new IllegalArgumentException("Student sa indeksom " + studProgramOznaka + studentBroj + "/" + studentGodina + " ne postoji.");
        }

        List<SlusaPredmet> slusaPredmeti = slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(studentIndeks.getId());
        return SlusaPredmetConverter.toSlusaPredmetResponseList(slusaPredmeti);
    }
}
