package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.repositories.PredispitneObavezeRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.utils.converters.PredispitneObavezeConverter;
import org.springframework.stereotype.Service;
import request.PredispitneObavezeRequest;
import response.PredispitneObavezeResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredispitneObavezeService {

    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final PredmetRepository predmetRepository;
    private final PredmetService predmetService;

    // CREATE
    public PredispitneObavezeResponse save(PredispitneObavezeRequest request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("ID ne sme biti poslat prilikom kreiranja nove predispitne obaveze.");
        }

        Optional<Predmet> predmetOpt = predmetRepository.findBySifra(request.getPredmetRequest().getSifra());
        if (!predmetOpt.isPresent()) {
            throw new IllegalArgumentException("Predmet sa šifrom " + request.getPredmetRequest().getSifra() + " ne postoji!");
        }
        Predmet predmet = predmetOpt.get();

        // Provera da li kombinacija već postoji
        List<PredispitneObaveze> postojece = predispitneObavezeRepository.findByPredmet(predmet);
        for (PredispitneObaveze p : postojece) {
            if (p.getVrstaPredispitneObaveze() == request.getVrstaPredispitneObaveze()
                    && p.getMaksimalanBrojPoena() == request.getMaksimalanBrojPoena()) {
                throw new IllegalArgumentException("Predispitna obaveza sa istim predmetom, vrstom i maksimalnim brojem poena već postoji!");
            }
        }

        PredispitneObaveze entity = new PredispitneObaveze();
        entity.setMaksimalanBrojPoena(request.getMaksimalanBrojPoena());
        entity.setVrstaPredispitneObaveze(request.getVrstaPredispitneObaveze());
        entity.setPredmet(predmet);

        PredispitneObaveze saved = predispitneObavezeRepository.save(entity);
        return PredispitneObavezeConverter.toResponse(saved);
    }


    // READ ALL
    public List<PredispitneObavezeResponse> findAll() {
        List<PredispitneObaveze> list = (List<PredispitneObaveze>) predispitneObavezeRepository.findAll();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Nema unetih predispitnih obaveza u bazi!");
        }
        return PredispitneObavezeConverter.toResponseList(list);
    }

    // READ BY ID
    public PredispitneObavezeResponse findById(Long id) {
        Optional<PredispitneObaveze> entityOpt = predispitneObavezeRepository.findById(id);
        if (!entityOpt.isPresent()) {
            throw new IllegalArgumentException("Predispitna obaveza sa ID " + id + " ne postoji.");
        }
        return PredispitneObavezeConverter.toResponse(entityOpt.get());
    }

    // READ BY PREDMET (preko šifre)
    public List<PredispitneObavezeResponse> findByPredmet(String sifraPredmeta) {
        Optional<Predmet> predmetOpt = predmetRepository.findBySifra(sifraPredmeta);
        if (!predmetOpt.isPresent()) {
            throw new IllegalArgumentException("Predmet sa šifrom " + sifraPredmeta + " ne postoji!");
        }

        List<PredispitneObaveze> list = predispitneObavezeRepository.findByPredmet(predmetOpt.get());
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Nema predispitnih obaveza za predmet " + sifraPredmeta);
        }

        return PredispitneObavezeConverter.toResponseList(list);
    }
    public PredispitneObavezeResponse update(PredispitneObavezeRequest request) {
        PredispitneObaveze entity;

        if (request.getId() != null) {
            // Update po ID
            Optional<PredispitneObaveze> entityOpt = predispitneObavezeRepository.findById(request.getId());
            if (!entityOpt.isPresent()) {
                throw new IllegalArgumentException("Predispitna obaveza sa ID " + request.getId() + " ne postoji.");
            }
            entity = entityOpt.get();
        } else {
            // Update po unikatnoj kombinaciji: predmet + vrsta + maksimalan broj poena
            Optional<Predmet> predmetOpt = predmetRepository.findBySifra(request.getPredmetRequest().getSifra());
            if (!predmetOpt.isPresent()) {
                throw new IllegalArgumentException("Predmet sa šifrom " + request.getPredmetRequest().getSifra() + " ne postoji!");
            }
            Predmet predmet = predmetOpt.get();

            List<PredispitneObaveze> postojece = predispitneObavezeRepository.findByPredmet(predmet);
            entity = null;
            for (PredispitneObaveze p : postojece) {
                if (p.getVrstaPredispitneObaveze() == request.getVrstaPredispitneObaveze()
                        && p.getMaksimalanBrojPoena() == request.getMaksimalanBrojPoena()) {
                    entity = p;
                    break;
                }
            }
            if (entity == null) {
                throw new IllegalArgumentException("Predispitna obaveza ne postoji za ažuriranje.");
            }
        }

        // Ažuriramo vrednosti
        entity.setMaksimalanBrojPoena(request.getMaksimalanBrojPoena());
        entity.setVrstaPredispitneObaveze(request.getVrstaPredispitneObaveze());

        Optional<Predmet> predmetOpt = predmetRepository.findBySifra(request.getPredmetRequest().getSifra());
        entity.setPredmet(predmetOpt.get());

        PredispitneObaveze updated = predispitneObavezeRepository.save(entity);
        return PredispitneObavezeConverter.toResponse(updated);
    }

    // DELETE
    public void delete(Long id) {
        Optional<PredispitneObaveze> entity = predispitneObavezeRepository.findById(id);
        if (!entity.isPresent()) {
            throw new IllegalArgumentException("Predispitna obaveza sa ID " + id + " ne postoji!");
        }
        predispitneObavezeRepository.deleteById(id);
    }
}
