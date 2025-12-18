package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.converters.OstvareniPredispitniConverter;
import org.springframework.stereotype.Service;
import request.OstvareniPredispitniRequest;
import response.OstvareniPredispitniResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OstvareniPredispitniService {

    private final OstvareniPredispitniRepository ostvareniPredispitniRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final PredmetRepository predmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;


    // CREATE
    public OstvareniPredispitniResponse save(OstvareniPredispitniRequest request) {
        // Dohvatanje entiteta preko ID-eva
        SlusaPredmet sp = slusaPredmetRepository.findById(request.getSlusaPredmetRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("SlusaPredmet ne postoji!"));
        PredispitneObaveze po = predispitneObavezeRepository.findById(request.getPredispitneObavezeRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("Predispitna obaveza ne postoji!"));

        // Proveri da li već postoji kombinacija (slusaPredmet + predispitna obaveza)
        List<OstvareniPredispitni> lista = ostvareniPredispitniRepository.findBySlusaPredmet(sp);
        for (OstvareniPredispitni o : lista) {
            if (o.getPredispitneObaveze().getId().equals(po.getId())) {
                throw new IllegalArgumentException("Već postoji unos za ovu kombinaciju slusaPredmet + predispitna obaveza!");
            }
        }

        // Kreiranje i snimanje novog OstvareniPredispitni entiteta
        OstvareniPredispitni entity = new OstvareniPredispitni();
        entity.setOstvareniPoeni(request.getOstvareniPoeni());
        entity.setSlusaPredmet(sp);
        entity.setPredispitneObaveze(po);

        OstvareniPredispitni saved = ostvareniPredispitniRepository.save(entity);
        return OstvareniPredispitniConverter.toResponse(saved);
    }


    // READ ALL
    public List<OstvareniPredispitniResponse> findAll() {
        List<OstvareniPredispitni> svi = (List<OstvareniPredispitni>) ostvareniPredispitniRepository.findAll();
        if (svi.isEmpty()) throw new IllegalArgumentException("Nema unosa!");
        return OstvareniPredispitniConverter.toResponseList(svi);
    }

    // READ BY ID
    public OstvareniPredispitniResponse findById(Long id) {
        Optional<OstvareniPredispitni> opt = ostvareniPredispitniRepository.findById(id);
        if (!opt.isPresent()) throw new IllegalArgumentException("Ne postoji zapis sa ID: " + id);
        return OstvareniPredispitniConverter.toResponse(opt.get());
    }

    // UPDATE
    public OstvareniPredispitniResponse update(OstvareniPredispitniRequest request) {
        SlusaPredmet sp = slusaPredmetRepository.findById(request.getSlusaPredmetRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("SlusaPredmet ne postoji!"));
        PredispitneObaveze po = predispitneObavezeRepository.findById(request.getPredispitneObavezeRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("Predispitna obaveza ne postoji!"));

        // Pronađi postojeći zapis po kombinaciji SlusaPredmet + PredispitneObaveze
        List<OstvareniPredispitni> lista = ostvareniPredispitniRepository.findBySlusaPredmet(sp);
        OstvareniPredispitni postojeci = null;
        for (OstvareniPredispitni o : lista) {
            if (o.getPredispitneObaveze().getId().equals(po.getId())) {
                postojeci = o;
                break;
            }
        }
        if (postojeci == null) throw new IllegalArgumentException("Zapis ne postoji za update!");

        // Ažuriranje ostvarenih poena
        postojeci.setOstvareniPoeni(request.getOstvareniPoeni());
        OstvareniPredispitni updated = ostvareniPredispitniRepository.save(postojeci);

        return OstvareniPredispitniConverter.toResponse(updated);
    }
    public List<OstvareniPredispitniResponse> getOstvareniPoeniZaStudentaNaPredmetu(
            Long studentIndeksId, Long predmetId, String skolskaGodina) {

        List<OstvareniPredispitni> lista = ostvareniPredispitniRepository
                .findOstvareniPoeniZaStudentaNaPredmetu(studentIndeksId, predmetId, skolskaGodina);

        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Nema unosa za datog studenta i predmet u ovoj školskoj godini");
        }

        return OstvareniPredispitniConverter.toResponseList(lista);
    }
    // DELETE
    public void delete(Long id) {
        Optional<OstvareniPredispitni> entity = ostvareniPredispitniRepository.findById(id);
        if (!entity.isPresent()) throw new IllegalArgumentException("Ne postoji zapis za ID: " + id);
        ostvareniPredispitniRepository.deleteById(id);
    }
}
