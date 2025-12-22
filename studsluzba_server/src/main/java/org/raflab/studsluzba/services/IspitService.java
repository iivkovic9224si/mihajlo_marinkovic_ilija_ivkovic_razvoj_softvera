package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.utils.converters.IspitConverter;
import org.springframework.stereotype.Service;
import request.IspitRequest;
import response.IspitResponse;
import response.RezultatiIspitaResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IspitService {

    private final IspitRepository ispitRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;

    public Optional<IspitResponse> save(IspitRequest request) {
        // Pronađi DrziPredmet
        Optional<DrziPredmet> drziPredmetOpt = drziPredmetRepository
                .findBySkolskaGodinaGodinaAndNastavnikEmailAndPredmetSifra(
                        request.getDrziPredmetRequest().getSkolskaGodinaNaziv(),
                        request.getDrziPredmetRequest().getNastavnikEmail(),
                        request.getDrziPredmetRequest().getPredmetSifra()
                );

        if (drziPredmetOpt.isEmpty()) {
            return Optional.empty();
        }
        DrziPredmet drziPredmet = drziPredmetOpt.get();

        // Odredi SkolskuGodinu (po imenu ili aktivnu)
        SkolskaGodina godina = drziPredmet.getSkolskaGodina();
        if (godina == null) {
            System.out.println("a");
            return Optional.empty();
        }

        // Pronađi IspitniRok koji pokriva datum ispita
        List<IspitniRok> rokovi = ispitniRokRepository.findBySkolskaGodinaAndDatum(godina, request.getDatumIspita());
        if (rokovi.isEmpty()) {
            System.out.println("b");
            return Optional.empty();
        }
        IspitniRok ispitniRok = rokovi.get(0);

        // Proveri da li ispit već postoji
        Optional<Ispit> existing = ispitRepository.findByDrziPredmetAndIspitniRok(drziPredmet, ispitniRok);
        if (existing.isPresent()) {
            System.out.println("c");
            return Optional.empty();
        }

        // Konvertuj i sačuvaj
        Ispit ispit = IspitConverter.toIspit(request, drziPredmet, ispitniRok);
        Ispit saved = ispitRepository.save(ispit);
        return Optional.of(IspitConverter.toIspitResponse(saved));
    }

    // U okviru IspitService klase
    public List<IspitResponse> findAllByIspitniRokId(Long ispitniRokId) {
        return ispitRepository.findByIspitniRokId(ispitniRokId)
                .stream()
                .map(IspitConverter::toIspitResponse)
                .collect(Collectors.toList());
    }

    public List<IspitResponse> findAll() {
        return ((List<Ispit>) ispitRepository.findAll())
                .stream()
                .map(IspitConverter::toIspitResponse)
                .collect(Collectors.toList());
    }

    public Optional<IspitResponse> findById(Long id) {
        return ispitRepository.findById(id)
                .map(IspitConverter::toIspitResponse);
    }

    public Optional<IspitResponse> update(IspitRequest request) {
        if (request.getId() == null) {
            return Optional.empty();
        }

        Optional<Ispit> ispitOpt = ispitRepository.findById(request.getId());
        if (ispitOpt.isEmpty()) {
            return Optional.empty();
        }
        Ispit ispit = ispitOpt.get();

        // Pronađi DrziPredmet
        Optional<DrziPredmet> drziPredmetOpt = drziPredmetRepository
                .findBySkolskaGodinaGodinaAndNastavnikEmailAndPredmetSifra(
                        request.getDrziPredmetRequest().getSkolskaGodinaNaziv(),
                        request.getDrziPredmetRequest().getNastavnikEmail(),
                        request.getDrziPredmetRequest().getPredmetSifra()
                );
        if (drziPredmetOpt.isEmpty()) {
            return Optional.empty();
        }
        DrziPredmet drziPredmet = drziPredmetOpt.get();

        // Odredi SkolskuGodinu
        SkolskaGodina godina = drziPredmet.getSkolskaGodina();
        if (godina == null) {
            return Optional.empty();
        }

        // Pronađi IspitniRok po datumu
        List<IspitniRok> rokovi = ispitniRokRepository.findBySkolskaGodinaAndDatum(godina, request.getDatumIspita());
        if (rokovi.isEmpty()) {
            return Optional.empty();
        }
        IspitniRok ispitniRok = rokovi.get(0);

        // Ažuriraj polja
        ispit.setDatumIspita(request.getDatumIspita());
        ispit.setVremePocetkaIspita(request.getVremePocetkaIspita());
        ispit.setZakljucen(request.isZakljucen());
        ispit.setDrziPredmet(drziPredmet);
        ispit.setIspitniRok(ispitniRok);

        Ispit saved = ispitRepository.save(ispit);
        return Optional.of(IspitConverter.toIspitResponse(saved));
    }

    public void delete(Long id) {
        if (ispitRepository.existsById(id)) {
            ispitRepository.deleteById(id);
        }
    }


    public List<RezultatiIspitaResponse> getRezultatiSaIspita(Long ispitId) {
        List<Object[]> res = izlazakNaIspitRepository.getRezultatiIspitaRawByIspitId(ispitId);

        List<RezultatiIspitaResponse> rezultati = new ArrayList<>();
        for (Object[] row : res) {
            rezultati.add(IspitConverter.toRezultatiIspitaResponse(row));
        }
        return rezultati;
    }


    //PROSECNA OCENA ISPITA
    public Double findProsecnaOcenaNaIspitu(Long ispitId) {
        return izlazakNaIspitRepository.getProsecnaOcenaNaIspituById(ispitId);
    }

}
