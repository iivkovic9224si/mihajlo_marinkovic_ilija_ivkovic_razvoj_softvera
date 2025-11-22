package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrziPredmetServiceTest {

    @Mock
    DrziPredmetRepository drziPredmetRepository;
    @Mock
    PredmetRepository predmetRepository;
    @Mock
    NastavnikRepository nastavnikRepository;
    @Mock
    SkolskaGodinaRepository skolskaGodinaRepository;

    @InjectMocks
    DrziPredmetService service;

    @Captor
    ArgumentCaptor<DrziPredmet> drziPredmetCaptor;

    private Nastavnik nastavnik;
    private Predmet predmet;
    private SkolskaGodina godina;

    @BeforeEach
    void setup() {
        nastavnik = new Nastavnik();
        nastavnik.setId(1L);
        nastavnik.setEmail("prof1@raf.rs");

        predmet = new Predmet();
        predmet.setId(1L);
        predmet.setSifra("MAT101");
        predmet.setNaziv("Matematika 1");

        godina = new SkolskaGodina();
        godina.setId(1L);
        godina.setGodina("2025/26");
        godina.setAktivna(true);
    }

    @Test
    @DisplayName("Create DrziPredmet success")
    void testSave() {
        DrziPredmetRequest request = new DrziPredmetRequest();
        request.setNastavnikEmail(nastavnik.getEmail());
        request.setPredmetSifra(predmet.getSifra());
        request.setSkolskaGodinaNaziv(godina.getGodina());

        when(nastavnikRepository.findByEmail(nastavnik.getEmail())).thenReturn(Optional.of(nastavnik));
        when(predmetRepository.findBySifra(predmet.getSifra())).thenReturn(Optional.of(predmet));
        when(skolskaGodinaRepository.findOneByGodina(godina.getGodina())).thenReturn(Optional.of(godina));

        DrziPredmet saved = new DrziPredmet();
        saved.setNastavnik(nastavnik);
        saved.setPredmet(predmet);
        saved.setSkolskaGodina(godina);

        when(drziPredmetRepository.save(any(DrziPredmet.class))).thenReturn(saved);

        DrziPredmetResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(nastavnik.getEmail(), response.getNastavnikEmail());
        assertEquals(godina.getGodina(), response.getSkolskaGodinaNaziv());
        assertEquals(predmet.getSifra(), response.getPredmetSifra());

        verify(drziPredmetRepository).save(drziPredmetCaptor.capture());
        DrziPredmet captured = drziPredmetCaptor.getValue();
        assertEquals(nastavnik.getEmail(), captured.getNastavnik().getEmail());
        assertEquals(predmet.getSifra(), captured.getPredmet().getSifra());
        assertEquals(godina.getGodina(), captured.getSkolskaGodina().getGodina());
    }

    @Test
    @DisplayName("Read all DrziPredmet")
    void testFindAll() {
        DrziPredmet dp = new DrziPredmet();
        dp.setNastavnik(nastavnik);
        dp.setPredmet(predmet);
        dp.setSkolskaGodina(godina);

        when(drziPredmetRepository.findAll()).thenReturn(List.of(dp));

        List<DrziPredmetResponse> list = service.findAll();
        assertEquals(1, list.size());

        DrziPredmetResponse response = list.get(0);
        assertEquals(godina.getGodina(), response.getSkolskaGodinaNaziv());
        assertEquals(nastavnik.getEmail(), response.getNastavnikEmail());
        assertEquals(predmet.getSifra(), response.getPredmetSifra());
    }

    @Test
    @DisplayName("Find by ID")
    void testFindById() {
        DrziPredmet dp = new DrziPredmet();
        dp.setNastavnik(nastavnik);
        dp.setPredmet(predmet);
        dp.setSkolskaGodina(godina);

        when(drziPredmetRepository.findById(1L)).thenReturn(Optional.of(dp));

        DrziPredmetResponse response = service.findById(1L);
        assertEquals(godina.getGodina(), response.getSkolskaGodinaNaziv());
        assertEquals(nastavnik.getEmail(), response.getNastavnikEmail());
        assertEquals(predmet.getSifra(), response.getPredmetSifra());
    }

    @Test
    @DisplayName("Update DrziPredmet")
    void testUpdate() {
        DrziPredmet dp = new DrziPredmet();
        dp.setNastavnik(nastavnik);
        dp.setPredmet(predmet);
        dp.setSkolskaGodina(godina);

        when(drziPredmetRepository.findById(1L)).thenReturn(Optional.of(dp));
        when(nastavnikRepository.findByEmail(nastavnik.getEmail())).thenReturn(Optional.of(nastavnik));
        when(predmetRepository.findBySifra(predmet.getSifra())).thenReturn(Optional.of(predmet));
        when(skolskaGodinaRepository.findOneByGodina(godina.getGodina())).thenReturn(Optional.of(godina));
        when(drziPredmetRepository.save(any(DrziPredmet.class))).thenReturn(dp);

        DrziPredmetRequest request = new DrziPredmetRequest();
        request.setNastavnikEmail(nastavnik.getEmail());
        request.setPredmetSifra(predmet.getSifra());
        request.setSkolskaGodinaNaziv(godina.getGodina());

        DrziPredmetResponse response = service.update(1L, request);

        assertEquals(godina.getGodina(), response.getSkolskaGodinaNaziv());
        assertEquals(nastavnik.getEmail(), response.getNastavnikEmail());
        assertEquals(predmet.getSifra(), response.getPredmetSifra());

        verify(drziPredmetRepository).save(drziPredmetCaptor.capture());
        DrziPredmet captured = drziPredmetCaptor.getValue();
        assertEquals(predmet.getSifra(), captured.getPredmet().getSifra());
    }

    @Test
    @DisplayName("Delete by ID")
    void testDeleteById() {
        DrziPredmet dp = new DrziPredmet();
        dp.setNastavnik(nastavnik);
        dp.setPredmet(predmet);
        dp.setSkolskaGodina(godina);

        when(drziPredmetRepository.findById(1L)).thenReturn(Optional.of(dp));

        service.deleteById(1L);

        verify(drziPredmetRepository).delete(drziPredmetCaptor.capture());
        assertEquals(nastavnik.getEmail(), drziPredmetCaptor.getValue().getNastavnik().getEmail());
    }
}
