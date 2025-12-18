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

import java.util.List;

@Service
@RequiredArgsConstructor
public class IspitniRokService {

    private final IspitniRokRepository ispitniRokRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @Transactional
    public IspitniRokResponse save(IspitniRokRequest request) {
        SkolskaGodina godina = resolveSkolskaGodina(request.getGodina());

        IspitniRok rok = IspitniRokConverter.toIspitniRok(request, godina);
        IspitniRok saved = ispitniRokRepository.save(rok);
        return IspitniRokConverter.toIspitniRokResponse(saved);
    }

    public List<IspitniRokResponse> findAll() {
        List<IspitniRok> rokovi = (List<IspitniRok>) ispitniRokRepository.findAll();
        return IspitniRokConverter.toIspitniRokResponseList(rokovi);
    }

    public List<IspitniRokResponse> findAllAktivnaGodina() {
        SkolskaGodina aktivnaGodina = skolskaGodinaRepository.findByAktivnaTrue();
        if (aktivnaGodina == null) {
            throw new IllegalArgumentException("Nema aktivne školske godine.");
        }
        List<IspitniRok> rokovi = ispitniRokRepository.findBySkolskaGodina(aktivnaGodina);
        return IspitniRokConverter.toIspitniRokResponseList(rokovi);
    }

    public IspitniRokResponse findById(Long id) {
        IspitniRok rok = ispitniRokRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ispitni rok sa ID " + id + " ne postoji."));
        return IspitniRokConverter.toIspitniRokResponse(rok);
    }

    @Transactional
    public IspitniRokResponse update(IspitniRokRequest request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("ID mora biti prosleđen za ažuriranje.");
        }

        IspitniRok rok;

        SkolskaGodina godina = resolveSkolskaGodina(request.getGodina());
        rok = IspitniRokConverter.toIspitniRok(request, godina);
        rok.setId(request.getId()); // zadržavamo ID

        IspitniRok updated = ispitniRokRepository.save(rok);
        return IspitniRokConverter.toIspitniRokResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if(!ispitniRokRepository.existsById(id)) {
            throw new IllegalArgumentException("Ispitni rok sa ID " + id + " ne postoji.");
        }
        ispitniRokRepository.deleteById(id);
    }

    private SkolskaGodina resolveSkolskaGodina(String godinaParam) {
        SkolskaGodina godina;
        if(godinaParam == null || godinaParam.isBlank()) {
            godina = skolskaGodinaRepository.findByAktivnaTrue();
            if(godina == null)
                throw new IllegalArgumentException("Nema aktivne školske godine.");
        } else {
            godina = skolskaGodinaRepository.findByGodina(godinaParam);
            if(godina == null)
                throw new IllegalArgumentException("Školska godina '" + godinaParam + "' ne postoji.");
        }
        return godina;
    }
}
