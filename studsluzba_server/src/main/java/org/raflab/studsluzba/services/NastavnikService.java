package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.utils.converters.NastavnikConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NastavnikService {

    @Autowired
    NastavnikRepository nastavnikRepository;

    public Nastavnik save(NastavnikRequest nastavnikRequest) {
        return nastavnikRepository.save(NastavnikConverter.toNastavnik(nastavnikRequest));
    }

    public Iterable<NastavnikResponse> findAll() {
        return NastavnikConverter.toNastavnikResponseList(nastavnikRepository.findAll());
    }

    public Optional<NastavnikResponse> findById(Long id) {
        return nastavnikRepository.findById(id).map(NastavnikConverter::toNastavnikResponse);
    }

    public List<NastavnikResponse> findByImeAndPrezime(String ime, String prezime) {
        return NastavnikConverter.toNastavnikResponseList(nastavnikRepository.findByImeAndPrezime(ime, prezime));
    }
}
