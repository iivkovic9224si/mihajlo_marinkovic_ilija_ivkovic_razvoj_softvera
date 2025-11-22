package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.controllers.response.IzlazakNaIspitResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.utils.converters.PolozenPredmetConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IzlazakNaIspitServiceTest {

    @Mock
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Mock
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Mock
    private PolozenPredmetRepository polozenPredmetRepository;

    @InjectMocks
    private IzlazakNaIspitService service;

    private PrijavaIspita prijava;
    private IzlazakNaIspit izlazak;

    @BeforeEach
    void setUp() {
        // Dummy entiteti
        prijava = new PrijavaIspita();
        prijava.setId(1L);

        SlusaPredmet sp = new SlusaPredmet();
        sp.setId(2L);
        prijava.setSlusaPredmet(sp);

        // Dodaj Ispit
        Ispit ispit = new Ispit();
        ispit.setId(3L);

        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setId(20L);
        ispit.setDrziPredmet(drziPredmet);

        prijava.setIspit(ispit);

        izlazak = new IzlazakNaIspit();
        izlazak.setId(1L);
        izlazak.setBrojPoenaSaIspita(30);
        izlazak.setBrojPredispitnihPoena(25);
        izlazak.setPrijavaIspita(prijava);
    }


    @Test
    void testSave_success() {
        IzlazakNaIspitRequest request = new IzlazakNaIspitRequest();
        request.setBrojPoenaSaIspita(30);
        request.setBrojPredispitnihPoena(25);
        request.setPrijavaIspitaID(1L);

        when(prijavaIspitaRepository.findById(1L)).thenReturn(Optional.of(prijava));
        when(izlazakNaIspitRepository.save(any(IzlazakNaIspit.class))).thenReturn(izlazak);

        IzlazakNaIspitResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(izlazak.getBrojPoenaSaIspita(), response.getBrojPoenaSaIspita());
    }

    public IzlazakNaIspitResponse saveWithPolozenPredmetIfPass(IzlazakNaIspitRequest request) {
        PrijavaIspita prijava = prijavaIspitaRepository.findById(request.getPrijavaIspitaID())
                .orElseThrow(() -> new RuntimeException("Prijava ne postoji"));

        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setBrojPoenaSaIspita(request.getBrojPoenaSaIspita());
        izlazak.setBrojPredispitnihPoena(request.getBrojPredispitnihPoena());
        izlazak.setPrijavaIspita(prijava);

        IzlazakNaIspit saved = izlazakNaIspitRepository.save(izlazak);

        if (saved.getBrojPoenaSaIspita() >= 24) { // primer uslova za polozen
            PolozenPredmet pp = new PolozenPredmet();
            pp.setStudentIndeks(prijava.getSlusaPredmet().getStudentIndeks());
            pp.setPredmet(prijava.getIspit().getDrziPredmet().getPredmet());
            pp.setPriznato(true);
            pp.setDatum(LocalDate.now());
            pp.setBrojPoena(saved.getBrojPoenaSaIspita() + saved.getBrojPredispitnihPoena());
            pp.setOcena(PolozenPredmetConverter.calculateOcena(pp.getBrojPoena()));
            polozenPredmetRepository.save(pp);
        }


        // Ovdje treba da se vrati response sa ID-em:
        IzlazakNaIspitResponse response = new IzlazakNaIspitResponse();
        response.setId(saved.getId());   // ← ovo možda fali
        response.setBrojPoenaSaIspita(saved.getBrojPoenaSaIspita());
        response.setBrojPredispitnihPoena(saved.getBrojPredispitnihPoena());
        return response;
    }


    @Test
    void testFindById_found() {
        when(izlazakNaIspitRepository.findById(1L)).thenReturn(Optional.of(izlazak));

        IzlazakNaIspitResponse response = service.findById(1L);
        assertNotNull(response);
        assertEquals(izlazak.getId(), response.getId());
    }

    @Test
    void testFindAll() {
        List<IzlazakNaIspit> list = new ArrayList<>();
        list.add(izlazak);

        when(izlazakNaIspitRepository.findAll()).thenReturn(list);

        List<IzlazakNaIspitResponse> responses = service.findAll();
        assertEquals(1, responses.size());
        assertEquals(izlazak.getId(), responses.get(0).getId());
    }

    @Test
    void testUpdate_success() {
        IzlazakNaIspitRequest request = new IzlazakNaIspitRequest();
        request.setBrojPoenaSaIspita(40);
        request.setBrojPredispitnihPoena(20);
        request.setPrijavaIspitaID(1L);

        when(izlazakNaIspitRepository.findById(1L)).thenReturn(Optional.of(izlazak));
        when(prijavaIspitaRepository.findById(1L)).thenReturn(Optional.of(prijava));
        when(izlazakNaIspitRepository.save(any(IzlazakNaIspit.class))).thenReturn(izlazak);

        IzlazakNaIspitResponse response = service.update(1L, request);

        assertNotNull(response);
        assertEquals(izlazak.getId(), response.getId());
        assertEquals(40, response.getBrojPoenaSaIspita());
    }

    @Test
    void testDelete() {
        doNothing().when(izlazakNaIspitRepository).deleteById(1L);

        service.delete(1L);

        verify(izlazakNaIspitRepository, times(1)).deleteById(1L);
    }
}
