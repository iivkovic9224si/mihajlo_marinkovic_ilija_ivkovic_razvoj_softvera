package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.controllers.response.RezultatiIspitaResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IspitServiceTest {

    @Mock
    private IspitRepository ispitRepository;
    @Mock
    private DrziPredmetRepository drziPredmetRepository;
    @Mock
    private IspitniRokRepository ispitniRokRepository;
    @Mock
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Mock
    private IzlazakNaIspitRepository izlazakNaIspitRepository;

    @InjectMocks
    private IspitService ispitService;

    private DrziPredmet drziPredmet;
    private SkolskaGodina skolskaGodina;
    private IspitniRok ispitniRok;
    private Ispit ispit;

    @BeforeEach
    void setUp() {
        skolskaGodina = new SkolskaGodina();
        skolskaGodina.setGodina("2025/26");

        drziPredmet = new DrziPredmet();
        drziPredmet.setPredmet(new Predmet());
        drziPredmet.getPredmet().setSifra("MAT101");
        drziPredmet.setNastavnik(new Nastavnik());
        drziPredmet.getNastavnik().setEmail("nastavnik@raf.rs");
        drziPredmet.setSkolskaGodina(skolskaGodina);

        ispitniRok = new IspitniRok();
        ispitniRok.setPocetak(LocalDate.of(2025, 10, 1));
        ispitniRok.setKraj(LocalDate.of(2025, 12, 31));
        ispitniRok.setSkolskaGodina(skolskaGodina);

        ispit = new Ispit();
        ispit.setId(1L);
        ispit.setDatumIspita(LocalDate.of(2025, 11, 25));
        ispit.setVremePocetkaIspita(LocalTime.of(10, 0));
        ispit.setZakljucen(false);
        ispit.setDrziPredmet(drziPredmet);
        ispit.setIspitniRok(ispitniRok);
    }

    @Test
    void testSave_success() {
        IspitRequest request = new IspitRequest();
        request.setDatumIspita(ispit.getDatumIspita());
        request.setVremePocetkaIspita(ispit.getVremePocetkaIspita());
        request.setZakljucen(false);

        DrziPredmetRequest drziRequest = new DrziPredmetRequest();
        drziRequest.setSkolskaGodinaNaziv("2025/26");
        drziRequest.setPredmetSifra("MAT101");
        drziRequest.setNastavnikEmail("nastavnik@raf.rs");
        request.setDrziPredmetRequest(drziRequest);

        when(drziPredmetRepository.findBySkolskaGodinaGodinaAndNastavnikEmailAndPredmetSifra(
                anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(drziPredmet));

        when(ispitniRokRepository.findBySkolskaGodinaAndDatum(any(), any())).thenReturn(List.of(ispitniRok));
        when(ispitRepository.findByDrziPredmetAndIspitniRok(any(), any())).thenReturn(Optional.empty());
        when(ispitRepository.save(any(Ispit.class))).thenReturn(ispit);

        Optional<IspitResponse> responseOpt = ispitService.save(request);
        assertTrue(responseOpt.isPresent());
        IspitResponse response = responseOpt.get();
        assertEquals(ispit.getDatumIspita(), response.getDatumIspita());
        assertEquals(ispit.getVremePocetkaIspita(), response.getVremePocetkaIspita());
    }

    @Test
    void testSave_drziPredmetNotFound() {
        IspitRequest request = new IspitRequest();
        request.setDrziPredmetRequest(new DrziPredmetRequest());
        Optional<IspitResponse> responseOpt = ispitService.save(request);
        assertTrue(responseOpt.isEmpty());
    }

    @Test
    void testUpdate_success() {
        IspitRequest request = new IspitRequest();
        request.setId(1L);
        request.setDatumIspita(LocalDate.of(2025, 11, 26));
        request.setVremePocetkaIspita(LocalTime.of(12, 0));
        request.setZakljucen(true);

        DrziPredmetRequest drziRequest = new DrziPredmetRequest();
        drziRequest.setSkolskaGodinaNaziv("2025/26");
        drziRequest.setPredmetSifra("MAT101");
        drziRequest.setNastavnikEmail("nastavnik@raf.rs");
        request.setDrziPredmetRequest(drziRequest);

        when(ispitRepository.findById(1L)).thenReturn(Optional.of(ispit));
        when(drziPredmetRepository.findBySkolskaGodinaGodinaAndNastavnikEmailAndPredmetSifra(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(drziPredmet));
        when(ispitniRokRepository.findBySkolskaGodinaAndDatum(any(), any())).thenReturn(List.of(ispitniRok));
        when(ispitRepository.save(any(Ispit.class))).thenReturn(ispit);

        Optional<IspitResponse> responseOpt = ispitService.update(request);
        assertTrue(responseOpt.isPresent());
        assertEquals(LocalDate.of(2025, 11, 26), responseOpt.get().getDatumIspita());
        assertTrue(responseOpt.get().isZakljucen());
    }

    @Test
    void testDelete() {
        when(ispitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ispitRepository).deleteById(1L);

        ispitService.delete(1L);
        verify(ispitRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        when(ispitRepository.findAll()).thenReturn(List.of(ispit));
        List<IspitResponse> all = ispitService.findAll();
        assertEquals(1, all.size());
        assertEquals(ispit.getDatumIspita(), all.get(0).getDatumIspita());
    }

    @Test
    void testFindById() {
        when(ispitRepository.findById(1L)).thenReturn(Optional.of(ispit));
        Optional<IspitResponse> responseOpt = ispitService.findById(1L);
        assertTrue(responseOpt.isPresent());
        assertEquals(ispit.getDatumIspita(), responseOpt.get().getDatumIspita());
    }

    @Test
    void testGetRezultatiSaIspita() {
        // Pravi listu nizova, svaki niz predstavlja red rezultata
        Object[] row = new Object[]{"Marko", "Markovic", "RI", 2, 123, 95.0};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(row);

        // Mockujemo repository da vrati ovu listu
        when(izlazakNaIspitRepository.getRezultatiIspitaRawByIspitId(1L))
                .thenReturn(mockResult);

        // Poziv servisa
        List<RezultatiIspitaResponse> rezultati = ispitService.getRezultatiSaIspita(1L);

        // Provera
        assertEquals(1, rezultati.size());
        assertEquals("Marko", rezultati.get(0).getImeStudenta());
        assertEquals(95.0, rezultati.get(0).getUkupnoPoena());
    }


    @Test
    void testFindProsecnaOcenaNaIspitu() {
        when(izlazakNaIspitRepository.getProsecnaOcenaNaIspituById(1L)).thenReturn(8.5);
        Double prosecna = ispitService.findProsecnaOcenaNaIspitu(1L);
        assertEquals(8.5, prosecna);
    }
}
