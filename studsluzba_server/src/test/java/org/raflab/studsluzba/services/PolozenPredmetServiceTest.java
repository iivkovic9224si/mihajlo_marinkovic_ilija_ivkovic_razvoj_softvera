package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.PolozenPredmetAwayRequest;
import org.raflab.studsluzba.controllers.request.PolozenPredmetHereRequest;
import org.raflab.studsluzba.controllers.response.PolozenPredmetResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolozenPredmetServiceTest {

    @Mock
    private PolozenPredmetRepository polozenPredmetRepository;
    @Mock
    private StudentIndeksRepository studentIndeksRepository;
    @Mock
    private PredmetRepository predmetRepository;
    @Mock
    private VisokoSkolskaUstanovaRepository visokoSkolskaUstanovaRepository;

    @InjectMocks
    private PolozenPredmetService polozenPredmetService;

    private StudentIndeks studentIndeks;
    private Predmet predmet;
    private VisokoSkolskaUstanova ustanova;

    @BeforeEach
    void setUp() {
        StudijskiProgram studProgram = new StudijskiProgram();
        studProgram.setOznaka("IT");

        studentIndeks = new StudentIndeks();
        studentIndeks.setId(1L);
        studentIndeks.setStudijskiProgram(studProgram);

        predmet = new Predmet();
        predmet.setId(1L);
        predmet.setStudProgram(studProgram);

        ustanova = new VisokoSkolskaUstanova();
        ustanova.setId(1L);
    }

    @Test
    void testSaveAway() {
        PolozenPredmetAwayRequest request = new PolozenPredmetAwayRequest(
                2025, 123, "IT", "MAT101", "VSU001",
                LocalDate.of(2025, 11, 22), 75, "Napomena"
        );

        when(studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(2025, 123, "IT", true))
                .thenReturn(Optional.of(studentIndeks));
        when(predmetRepository.findBySifra("MAT101")).thenReturn(Optional.of(predmet));
        when(visokoSkolskaUstanovaRepository.findBySifra("VSU001")).thenReturn(Optional.of(ustanova));

        PolozenPredmetResponse response = polozenPredmetService.saveAway(request);

        assertNotNull(response);
        assertEquals(8, response.getOcena());
        assertEquals("Napomena", response.getNapomena());

        ArgumentCaptor<PolozenPredmet> captor = ArgumentCaptor.forClass(PolozenPredmet.class);
        verify(polozenPredmetRepository).save(captor.capture());
        PolozenPredmet saved = captor.getValue();
        assertTrue(saved.isPriznato());
        assertEquals(ustanova, saved.getPriznatoOdInstitucije());
        assertEquals(studentIndeks, saved.getStudentIndeks());
        assertEquals(predmet, saved.getPredmet());
        assertEquals(75, saved.getBrojPoena());
        assertEquals(8, saved.getOcena());
        assertEquals("Napomena", saved.getNapomena());
        assertEquals(LocalDate.of(2025, 11, 22), saved.getDatum());
    }

    @Test
    void testSaveHere() {
        PolozenPredmetHereRequest request = new PolozenPredmetHereRequest(
                2025, 123, "IT", "MAT101", 1L, "Napomena ovde"
        );

        when(studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(2025, 123, "IT", true))
                .thenReturn(Optional.of(studentIndeks));
        when(predmetRepository.findBySifra("MAT101")).thenReturn(Optional.of(predmet));

        PolozenPredmetResponse response = polozenPredmetService.saveHere(request);

        assertNotNull(response);
        assertEquals("Napomena ovde", response.getNapomena());

        ArgumentCaptor<PolozenPredmet> captor = ArgumentCaptor.forClass(PolozenPredmet.class);
        verify(polozenPredmetRepository).save(captor.capture());
        PolozenPredmet saved = captor.getValue();
        assertFalse(saved.isPriznato());
        assertNull(saved.getDatum());
        assertEquals(studentIndeks, saved.getStudentIndeks());
        assertEquals(predmet, saved.getPredmet());
        assertEquals("Napomena ovde", saved.getNapomena());
        assertEquals(0.0, saved.getBrojPoena());
        assertNull(saved.getOcena());
    }

    @Test
    void testFindById() {
        PolozenPredmet pp = new PolozenPredmet();
        pp.setId(1L);
        pp.setNapomena("Test napomena");
        pp.setPredmet(predmet);
        pp.setStudentIndeks(studentIndeks);

        when(polozenPredmetRepository.findById(1L)).thenReturn(Optional.of(pp));

        PolozenPredmetResponse response = polozenPredmetService.findById(1L);

        assertNotNull(response);
        assertEquals("Test napomena", response.getNapomena());
        verify(polozenPredmetRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        PolozenPredmet pp = new PolozenPredmet();
        pp.setId(1L);
        pp.setNapomena("Napomena");
        pp.setPredmet(predmet);
        pp.setStudentIndeks(studentIndeks);

        when(polozenPredmetRepository.findAll()).thenReturn(Collections.singletonList(pp));

        var all = polozenPredmetService.findAll();

        assertEquals(1, all.size());
        assertEquals("Napomena", all.get(0).getNapomena());
        verify(polozenPredmetRepository).findAll();
    }

    @Test
    void testDelete() {
        polozenPredmetService.delete(1L);
        verify(polozenPredmetRepository).deleteById(1L);
    }
}
