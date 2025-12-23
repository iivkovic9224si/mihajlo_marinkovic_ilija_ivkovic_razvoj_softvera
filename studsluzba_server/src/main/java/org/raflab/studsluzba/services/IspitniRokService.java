package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.utils.converters.IspitniRokConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.IspitniRokRequest;
import response.IspitniRokResponse;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IspitniRokService {

    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    // ===================== CREATE =====================
    @Transactional
    public IspitniRokResponse save(IspitniRokRequest request) {
        SkolskaGodina godina = resolveAndValidateGodina(
                request.getGodina(),
                request.getPocetak(),
                request.getKraj()
        );

        IspitniRok rok = IspitniRokConverter.toIspitniRok(request, godina);
        IspitniRok saved = ispitniRokRepository.save(rok);
        return IspitniRokConverter.toIspitniRokResponse(saved);
    }

    // ===================== UPDATE =====================
    @Transactional
    public IspitniRokResponse update(IspitniRokRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID mora biti prosleđen za ažuriranje.");
        }

        SkolskaGodina godina = resolveAndValidateGodina(
                request.getGodina(),
                request.getPocetak(),
                request.getKraj()
        );

        IspitniRok rok = IspitniRokConverter.toIspitniRok(request, godina);
        rok.setId(request.getId());

        IspitniRok updated = ispitniRokRepository.save(rok);
        return IspitniRokConverter.toIspitniRokResponse(updated);
    }

    // ===================== READ =====================
    public List<IspitniRokResponse> findAll() {
        return IspitniRokConverter.toIspitniRokResponseList(
                (List<IspitniRok>) ispitniRokRepository.findAll()
        );
    }

    public List<IspitniRokResponse> findAllAktivnaGodina() {
        SkolskaGodina aktivna = skolskaGodinaRepository.findByAktivnaTrue();
        if (aktivna == null) {
            throw new IllegalArgumentException("Nema aktivne školske godine.");
        }
        return IspitniRokConverter.toIspitniRokResponseList(
                ispitniRokRepository.findBySkolskaGodina(aktivna)
        );
    }

    public IspitniRokResponse findById(Long id) {
        IspitniRok rok = ispitniRokRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ispitni rok ne postoji."));
        return IspitniRokConverter.toIspitniRokResponse(rok);
    }

    // ===================== DELETE =====================
    @Transactional
    public void delete(Long id) {
        if (!ispitniRokRepository.existsById(id)) {
            throw new IllegalArgumentException("Ispitni rok ne postoji.");
        }
        ispitniRokRepository.deleteById(id);
    }

    // =================================================
    // =============== VALIDATION CORE =================
    // =================================================

    private SkolskaGodina resolveAndValidateGodina(
            String godinaParam,
            LocalDate pocetak,
            LocalDate kraj
    ) {

        if (pocetak.isAfter(kraj)) {
            throw new IllegalArgumentException("Datum početka ne može biti posle datuma kraja.");
        }

        SkolskaGodina godina;

        // 1. Ako nije poslata godina → aktivna
        if (godinaParam == null || godinaParam.isBlank()) {
            godina = skolskaGodinaRepository.findByAktivnaTrue();
            if (godina == null) {
                throw new IllegalArgumentException("Nema aktivne školske godine.");
            }
        } else {
            godina = skolskaGodinaRepository.findByGodina(godinaParam);
            if (godina == null) {
                throw new IllegalArgumentException("Školska godina '" + godinaParam + "' ne postoji.");
            }
        }

        // 2. Parsiranje formata 2025/2026
        String[] parts = godina.getGodina().split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Neispravan format školske godine.");
        }

        int startYear;
        int endYear;
        try {
            startYear = Integer.parseInt(parts[0]);
            endYear = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Neispravan format školske godine.");
        }

        if (endYear != startYear + 1) {
            throw new IllegalArgumentException("Neispravan raspon školske godine.");
        }

        // 3. Dozvoljeni opseg datuma
        LocalDate allowedStart = LocalDate.of(startYear, 10, 1);
        LocalDate allowedEnd = LocalDate.of(endYear, 9, 30);

        if (pocetak.isBefore(allowedStart) || kraj.isAfter(allowedEnd)) {
            throw new IllegalArgumentException(
                    "Datumi ispitnog roka ne pripadaju školskoj godini " + godina.getGodina()
            );
        }

        return godina;
    }
}
