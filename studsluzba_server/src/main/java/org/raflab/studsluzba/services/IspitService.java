package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.RezultatiIspitaResponse;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.utils.converters.IspitConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IspitService {

    @Autowired
    IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private IspitRepository ispitRepository;

    public Optional<Ispit> findById(Long id) {
        return ispitRepository.findById(id);
    }

    public List<RezultatiIspitaResponse> getRezultatiSaIspita(Ispit ispit) {
        List<Object[]> res = izlazakNaIspitRepository.getRezultatiIspitaRaw(ispit);
        return res.stream().map(IspitConverter::toRezultatiIspitaDTO).collect(Collectors.toList());
    }

    public Double findProsecnaOcenaNaIspitu(Ispit ispit) {
        return izlazakNaIspitRepository.getProsecnaOcenaNaIspitu(ispit);
    }



}
