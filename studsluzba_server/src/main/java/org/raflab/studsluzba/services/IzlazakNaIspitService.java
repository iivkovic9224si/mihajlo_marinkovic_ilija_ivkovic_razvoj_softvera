package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.PrijavaIspita;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.utils.converters.IzlazakNaIspitConverter;
import org.raflab.studsluzba.utils.converters.PrijavaIspitaConverter;
import org.springframework.stereotype.Service;
import request.IzlazakNaIspitRequest;
import response.IzlazakNaIspitResponse;
import response.PrijavaIspitaResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IzlazakNaIspitService {

    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final PolozenPredmetRepository polozenPredmetRepository;

    // SAVE - kreira novi
    public IzlazakNaIspitResponse save(IzlazakNaIspitRequest request) {
        if (request.getId() != null) {
            throw new RuntimeException("ID must be null for save");
        }

        if (izlazakNaIspitRepository.existsByPrijavaIspitaId(request.getPrijavaIspitaID())) {
            throw new RuntimeException("Izlazak za ovu prijavu je već evidentiran.");
        }

        PrijavaIspita prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaID())
                .orElseThrow(() -> new RuntimeException("Prijava ispita ne postoji"));

        IzlazakNaIspit izlazak = IzlazakNaIspitConverter.toEntity(request, prijava);
        izlazak = izlazakNaIspitRepository.save(izlazak);

        // ✅ KLJUČNO: da pi.izlazakNaIspita više ne bude null
        prijava.setIzlazakNaIspita(izlazak);
        prijavaIspitaRepository.save(prijava);

        return IzlazakNaIspitConverter.toResponse(izlazak);
    }


    public IzlazakNaIspitResponse saveWithPolozenPredmetIfPass(IzlazakNaIspitRequest request) {
        if (request.getId() != null) {
            throw new RuntimeException("ID must be null for save");
        }

        if (izlazakNaIspitRepository.existsByPrijavaIspitaId(request.getPrijavaIspitaID())) {
            throw new RuntimeException("Izlazak za ovu prijavu je već evidentiran.");
        }

        PrijavaIspita prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaID())
                .orElseThrow(() -> new RuntimeException("Prijava ispita ne postoji"));

        IzlazakNaIspit izlazak = IzlazakNaIspitConverter.toEntity(request, prijava);
        double ukupnoPoena = izlazak.getBrojPoenaSaIspita() + izlazak.getBrojPredispitnihPoena();

        // 1) Sačuvaj izlazak
        izlazak = izlazakNaIspitRepository.save(izlazak);

        // ✅ 2) Poveži ga sa prijavom (da nestane iz "prijave za izlazak")
        prijava.setIzlazakNaIspita(izlazak);
        prijavaIspitaRepository.save(prijava);

        // 3) Ako je položio, kreiraj PolozenPredmet
        if (ukupnoPoena >= 51) {
            PolozenPredmet pp = new PolozenPredmet();

            pp.setBrojPoena(ukupnoPoena);
            pp.setDatum(LocalDate.now());
            pp.setIzlazakIspita(izlazak);

            // Popuni entitete StudentIndeks i Predmet
            pp.setStudentIndeks(prijava.getSlusaPredmet().getStudentIndeks());
            pp.setPredmet(prijava.getIspit().getDrziPredmet().getPredmet());

            // ✅ DODAJ OVO (pre save):
            pp.setOcena(izracunajOcenu(ukupnoPoena));

            polozenPredmetRepository.save(pp);

            // Poveži izlazak sa položenim predmetom
            izlazak.setPolozenPredmet(pp);
            izlazakNaIspitRepository.save(izlazak);
        }


        return IzlazakNaIspitConverter.toResponse(izlazak);
    }

    private int izracunajOcenu(double ukupnoPoena) {
        if (ukupnoPoena < 51) return 5;
        if (ukupnoPoena <= 60) return 6;
        if (ukupnoPoena <= 70) return 7;
        if (ukupnoPoena <= 80) return 8;
        if (ukupnoPoena <= 90) return 9;
        return 10;
    }


    public IzlazakNaIspitResponse findById(Long id) {
        Optional<IzlazakNaIspit> e = izlazakNaIspitRepository.findById(id);
        return e.isPresent() ? IzlazakNaIspitConverter.toResponse(e.get()) : null;
    }

    public List<IzlazakNaIspitResponse> findAll() {
        List<IzlazakNaIspitResponse> res = new ArrayList<>();
        Iterable<IzlazakNaIspit> svi = izlazakNaIspitRepository.findAll();
        for (IzlazakNaIspit e : svi) {
            res.add(IzlazakNaIspitConverter.toResponse(e));
        }
        return res;
    }
    // UPDATE - menja postojeći
    public IzlazakNaIspitResponse update(Long id, IzlazakNaIspitRequest request) {
        Optional<IzlazakNaIspit> existing = izlazakNaIspitRepository.findById(id);
        if (!existing.isPresent()) {
            throw new RuntimeException("Izlazak na ispit sa ID " + id + " ne postoji");
        }

        Optional<PrijavaIspita> prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaID());
        if (!prijava.isPresent()) {
            throw new RuntimeException("Prijava ispita ne postoji");
        }

        IzlazakNaIspit e = existing.get();
        e.setBrojPoenaSaIspita(request.getBrojPoenaSaIspita());
        e.setBrojPredispitnihPoena(request.getBrojPredispitnihPoena());
        e.setNapomena(request.getNapomena());
        e.setPonistava(request.isPonistava());
        e.setPrijavaIspita(prijava.get());

        izlazakNaIspitRepository.save(e);
        return IzlazakNaIspitConverter.toResponse(e);
    }

    public void delete(Long id) {
        izlazakNaIspitRepository.deleteById(id);
    }

    // dodatni upit: svi izlazci za datu prijavu
    public List<IzlazakNaIspitResponse> findByPrijavaIspitaId(Long prijavaId) {
        List<IzlazakNaIspit> izlazci = izlazakNaIspitRepository.findByPrijavaIspitaId(prijavaId);
        List<IzlazakNaIspitResponse> res = new ArrayList<>();
        for (IzlazakNaIspit e : izlazci) {
            res.add(IzlazakNaIspitConverter.toResponse(e));
        }
        return res;
    }

    public Long countStudentPolagaoPredmet(Long studentId, Long drziPredmetId) {
        return izlazakNaIspitRepository.countIzlasciZaStudentaNaPredmet(studentId, drziPredmetId);
    }

    public List<IzlazakNaIspitResponse> findIzlasciZaStudenta(Integer godina, Integer broj, String oznaka) {
        return izlazakNaIspitRepository.findIzlasciZaStudenta(godina, broj, oznaka)
                .stream().map(IzlazakNaIspitConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<PrijavaIspitaResponse> findPrijaveZaIzlazakZaStudenta(Integer godina, Integer broj, String oznaka) {
        return izlazakNaIspitRepository.findPrijaveZaIzlazakZaStudenta(godina, broj, oznaka)
                .stream()
                .map(PrijavaIspitaConverter::toPrijavaIspitaResponse)
                .collect(Collectors.toList());
    }
}
