package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.converters.SlusaPredmetConverter;
import org.raflab.studsluzba.utils.converters.StudijskiTokAktivnostiConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;

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

    //upis studenta na godinu (dodati novi upis za studenta i predmete koje sluša, a koji će inicijalno biti nepoloženi)
    public void upisiStudentaNaGodinu(StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest, List<DrziPredmetRequest> drziPredmetRequests) {
        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(studijskiTokAktivnostiRequest.getIndeksStudProgramOznaka(),studijskiTokAktivnostiRequest.getIndeksGodina(),studijskiTokAktivnostiRequest.getIndeksBroj());
        if(studentIndeks == null){
            throw new RuntimeException("Student sa tim indeksom nije pronadjen");
        }
        studijskiTokAktivnostiRepository.save(StudijskiTokAktivnostiConverter.toStrudijskiTokAktivnosti(studijskiTokAktivnostiRequest, studentIndeks));

        List<DrziPredmet> drziPredmets = new ArrayList<>();
        for(DrziPredmetRequest drziPredmetRequest : drziPredmetRequests){
            DrziPredmet drziPredmet = drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra(drziPredmetRequest.getNastavnikEmail(),drziPredmetRequest.getPredmetSifra());
            if(drziPredmet == null){
                throw new RuntimeException("Predmet pod sifrom "+ drziPredmetRequest.getPredmetSifra() +" koji predaje profesor "+ drziPredmetRequest.getNastavnikEmail()+", nije pronadjen");
            }
            drziPredmets.add(drziPredmet);
        }

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue();
        for(DrziPredmet drziPredmet : drziPredmets){
            SlusaPredmet slusaPredmet = SlusaPredmetConverter.toSlusaPredmet(skolskaGodina,studentIndeks,drziPredmet);
            slusaPredmetRepository.save(slusaPredmet);
        }
    }

    // obnova godine za studenta (nepoloženi + izbor predmeta iz naredne godine, max 60 ESPB)
    public void obnovaGodine(StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest,
                             List<DrziPredmetRequest> drziPredmetRequests) {

        StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(studijskiTokAktivnostiRequest.getIndeksStudProgramOznaka(),studijskiTokAktivnostiRequest.getIndeksGodina(),studijskiTokAktivnostiRequest.getIndeksBroj());
        if(studentIndeks == null){
            throw new RuntimeException("Student sa tim indeksom nije pronadjen");
        }

        studijskiTokAktivnostiRepository.save(StudijskiTokAktivnostiConverter.toStrudijskiTokAktivnosti(studijskiTokAktivnostiRequest, studentIndeks));


        List<DrziPredmet> drziPredmets = new ArrayList<>();
        int ukupnoESPB = 0;

        for (DrziPredmetRequest drziPredmetRequest : drziPredmetRequests) {

            DrziPredmet drziPredmet = drziPredmetRepository
                    .getDrziPredmetByNastavnikMailAndPredmetSifra(
                            drziPredmetRequest.getNastavnikEmail(),
                            drziPredmetRequest.getPredmetSifra()
                    );

            if (drziPredmet == null) {
                throw new RuntimeException("Predmet pod sifrom " + drziPredmetRequest.getPredmetSifra() +
                        " koji predaje profesor " + drziPredmetRequest.getNastavnikEmail() + " nije pronađen.");
            }

            ukupnoESPB += drziPredmet.getPredmet().getEspb();

            if (ukupnoESPB > 60) {
                throw new RuntimeException("Ukupan broj ESPB ne može preći 60! Trenutno: " + ukupnoESPB);
            }

            drziPredmets.add(drziPredmet);
        }

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue();

        for (DrziPredmet drziPredmet : drziPredmets) {
            SlusaPredmet slusaPredmet = SlusaPredmetConverter.toSlusaPredmet(
                    skolskaGodina, studentIndeks, drziPredmet
            );
            slusaPredmetRepository.save(slusaPredmet);
        }
    }

}
