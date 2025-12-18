package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.utils.converters.SkolskaGodinaConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.SkolskaGodinaRequest;
import response.SkolskaGodinaResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkolskaGodinaService {

    private final SkolskaGodinaRepository skolskaGodinaRepository;

    // CREATE
    @Transactional
    public SkolskaGodinaResponse save(SkolskaGodinaRequest skolskaGodinaRequest) throws Exception {
        if (skolskaGodinaRequest.getGodina() == null || skolskaGodinaRequest.getGodina().isBlank()) {
            throw new IllegalArgumentException("Godina ne sme biti prazna.");
        }
        if (skolskaGodinaRequest.isAktivna()) {
            skolskaGodinaRepository.deactivateAll();
        }

        SkolskaGodina godina = SkolskaGodinaConverter.toSkolskaGodina(skolskaGodinaRequest);
        SkolskaGodina saved = skolskaGodinaRepository.save(godina);
        return SkolskaGodinaConverter.toSkolskaGodinaResponse(saved);
    }

    public List<SkolskaGodinaResponse> findAll() {
        List<SkolskaGodina> godine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();
        if (godine.isEmpty()) {
            throw new IllegalArgumentException("Nema školskih godina u bazi.");
        }
        return SkolskaGodinaConverter.toSkolskaGodinaResponseList(godine);
    }

    public SkolskaGodinaResponse findById(Long id) {
        SkolskaGodina godina = skolskaGodinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Školska godina sa ID " + id + " ne postoji."));
        return SkolskaGodinaConverter.toSkolskaGodinaResponse(godina);
    }

    @Transactional
    public SkolskaGodinaResponse update(SkolskaGodinaRequest skolskaGodinaRequest) {
        if (skolskaGodinaRequest.getGodina() == null || skolskaGodinaRequest.getGodina().isBlank()) {
            throw new IllegalArgumentException("Godina ne sme biti prazna.");
        }
        SkolskaGodina godina = skolskaGodinaRepository.findByGodina(skolskaGodinaRequest.getGodina());
        if (godina == null) {
            throw new IllegalArgumentException("Školska godina '" + skolskaGodinaRequest.getGodina() + "' ne postoji.");
        }
        godina.setAktivna(skolskaGodinaRequest.isAktivna());
        skolskaGodinaRepository.save(godina);
        SkolskaGodina aktivnaGodina = skolskaGodinaRepository.findByAktivnaTrue();
        if (aktivnaGodina == null) {
            List<SkolskaGodina> sveGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();
            sveGodine.sort((a, b) -> {
                int godinaA = Integer.parseInt(a.getGodina().split("/")[0]);
                int godinaB = Integer.parseInt(b.getGodina().split("/")[0]);
                return Integer.compare(godinaB, godinaA); // najveća prva
            });

            SkolskaGodina poslednja = sveGodine.get(0);
            poslednja.setAktivna(true);
            skolskaGodinaRepository.save(poslednja);
            return SkolskaGodinaConverter.toSkolskaGodinaResponse(poslednja);
        }

        return SkolskaGodinaConverter.toSkolskaGodinaResponse(godina);
    }

    public void delete(Long id) {
        if (!skolskaGodinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Školska godina sa ID " + id + " ne postoji!");
        }
        skolskaGodinaRepository.deleteById(id);
    }

    public SkolskaGodinaResponse findActiveYear() {
        SkolskaGodina aktivna = skolskaGodinaRepository.findByAktivnaTrue();
        if (aktivna == null) {
            throw new IllegalArgumentException("Trenutno nema aktivne školske godine.");
        }
        return SkolskaGodinaConverter.toSkolskaGodinaResponse(aktivna);
    }
}
