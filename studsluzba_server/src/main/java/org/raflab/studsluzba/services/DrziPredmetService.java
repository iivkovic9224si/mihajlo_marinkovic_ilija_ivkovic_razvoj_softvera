package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrziPredmetService {

    private final DrziPredmetRepository drziPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final NastavnikRepository nastavnikRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    //----------------------CREATE----------------------
    @Transactional
    public DrziPredmetResponse save(DrziPredmetRequest request) {
        Nastavnik nastavnik = nastavnikRepository.findByEmail(request.getNastavnikEmail())
                .orElseThrow(() -> new RuntimeException("Nastavnik nije pronadjen: " + request.getNastavnikEmail()));

        Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                .orElseThrow(() -> new RuntimeException("Predmet nije pronadjen: " + request.getPredmetSifra()));

        SkolskaGodina godina = skolskaGodinaRepository.findOneByGodina(request.getSkolskaGodinaNaziv())
                .orElseThrow(() -> new RuntimeException("Skolska godina nije pronadjena: " + request.getSkolskaGodinaNaziv()));

        DrziPredmet drziPredmet = Converters.toDrziPredmet(request, nastavnik, predmet, godina);

        DrziPredmet saved = drziPredmetRepository.save(drziPredmet);

        return Converters.toDrziPredmetResponse(saved);
    }

    //----------------------READ----------------------
    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> findAll() {
        return Converters.toDrziPredmetResponseList(drziPredmetRepository.findAll());
    }

    @Transactional(readOnly = true)
    public DrziPredmetResponse findById(Long id) {
        DrziPredmet dp = drziPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DrziPredmet ne postoji: " + id));
        return Converters.toDrziPredmetResponse(dp);
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> findByNastavnikEmail(String nastavnikEmail) {
        Iterable<DrziPredmet> all = drziPredmetRepository.findAll();
        List<DrziPredmet> list = new ArrayList<>();
        all.forEach(list::add); // Iterable -> List

        return list.stream()
                .filter(dp -> dp.getNastavnik() != null && dp.getNastavnik().getEmail().equals(nastavnikEmail))
                .map(Converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<PredmetResponse> getPredmetiZaNastavnikaUAktivnojGodini(String nastavnikEmail) {
        return Converters.toPredmetResponseList(drziPredmetRepository.getPredmetiZaNastavnikaUAktivnojSkolskojGodini(nastavnikEmail));
    }

    //----------------------UPDATE----------------------
    @Transactional
    public DrziPredmetResponse update(Long id, DrziPredmetRequest request) {
        DrziPredmet drziPredmet = drziPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DrziPredmet ne postoji: " + id));

        if (request.getNastavnikEmail() != null) {
            Nastavnik nastavnik = nastavnikRepository.findByEmail(request.getNastavnikEmail())
                    .orElseThrow(() -> new RuntimeException("Nastavnik nije pronadjen: " + request.getNastavnikEmail()));
            drziPredmet.setNastavnik(nastavnik);
        }

        if (request.getPredmetSifra() != null) {
            Predmet predmet = predmetRepository.findBySifra(request.getPredmetSifra())
                    .orElseThrow(() -> new RuntimeException("Predmet nije pronadjen: " + request.getPredmetSifra()));
            drziPredmet.setPredmet(predmet);
        }

        if (request.getSkolskaGodinaNaziv() != null) {
            SkolskaGodina godina = skolskaGodinaRepository.findOneByGodina(request.getSkolskaGodinaNaziv())
                    .orElseThrow(() -> new RuntimeException("Skolska godina nije pronadjena: " + request.getSkolskaGodinaNaziv()));
            drziPredmet.setSkolskaGodina(godina);
        }

        DrziPredmet updated = drziPredmetRepository.save(drziPredmet);
        return Converters.toDrziPredmetResponse(updated);
    }

    //----------------------DELETE----------------------
    @Transactional
    public void deleteById(Long id) {
        DrziPredmet drziPredmet = drziPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DrziPredmet ne postoji: " + id));
        drziPredmetRepository.delete(drziPredmet);
    }
}
