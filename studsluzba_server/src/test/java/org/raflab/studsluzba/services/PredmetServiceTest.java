package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredmetServiceTest {

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private StudijskiProgramRepository studijskiProgramRepository;

    @Mock
    private PolozenPredmetRepository polozenPredmetRepository;

    @InjectMocks
    private PredmetService service;

    private PredmetRequest request;
    private Predmet predmet;
    private StudijskiProgram studProgram;

    @BeforeEach
    void setUp() {
        studProgram = new StudijskiProgram();
        studProgram.setOznaka("SP1");

        request = new PredmetRequest();
        request.setSifra("PR40");
        request.setNaziv("Matematika");
        request.setEspb(6);
        request.setObavezan(true);
        request.setOznakaStudProgram("SP1");

        predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setEspb(request.getEspb());
        predmet.setObavezan(request.isObavezan());
        predmet.setStudProgram(studProgram);
    }

    @Test
    void testSaveSuccess() {
        when(studijskiProgramRepository.findByOznakaSingle("SP1")).thenReturn(studProgram);
        when(predmetRepository.save(any(Predmet.class))).thenReturn(predmet);

        PredmetResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(request.getSifra(), response.getSifra());
        assertEquals(request.getNaziv(), response.getNaziv());
        verify(predmetRepository, times(1)).save(any(Predmet.class));
    }

    @Test
    void testSaveInvalidStudProgramThrows() {
        request.setOznakaStudProgram(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertTrue(ex.getMessage().contains("ne sme biti prazna"));
    }

    @Test
    void testFindAllSuccess() {
        when(predmetRepository.findAll()).thenReturn(List.of(predmet));

        List<PredmetResponse> list = service.findAll();
        assertEquals(1, list.size());
    }

    @Test
    void testFindByIdSuccess() {
        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));
        PredmetResponse response = service.findById(1L);
        assertEquals(predmet.getSifra(), response.getSifra());
    }

    @Test
    void testFindByIdNotFound() {
        when(predmetRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.findById(1L));
        assertTrue(ex.getMessage().contains("ne postoji"));
    }

    @Test
    void testUpdateSuccess() {
        when(predmetRepository.findBySifra("PR40")).thenReturn(Optional.of(predmet));
        when(studijskiProgramRepository.findByOznakaSingle("SP1")).thenReturn(studProgram);
        when(predmetRepository.save(any(Predmet.class))).thenReturn(predmet);

        request.setNaziv("Fizika");
        PredmetResponse response = service.update("PR40", request);
        assertEquals("Fizika", response.getNaziv());
    }

    @Test
    void testDeleteSuccess() {
        when(predmetRepository.existsById(1L)).thenReturn(true);
        doNothing().when(predmetRepository).deleteById(1L);

        service.delete(1L);
        verify(predmetRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByGodinaAkreditacijeSuccess() {
        when(predmetRepository.getPredmetForGodinaAkreditacije(2024)).thenReturn(List.of(predmet));
        List<PredmetResponse> list = service.findByGodinaAkreditacije(2024);
        assertEquals(1, list.size());
    }

    @Test
    void testFindByStudProgramAndObavezanSuccess() {
        when(studijskiProgramRepository.findByOznakaSingle("SP1")).thenReturn(studProgram);
        when(predmetRepository.getPredmetsByStudProgramAndObavezan(studProgram, true)).thenReturn(List.of(predmet));

        List<PredmetResponse> list = service.findByStudProgramAndObavezan("SP1", true);
        assertEquals(1, list.size());
    }

    @Test
    void testFindBySifraSuccess() {
        when(predmetRepository.findBySifra("PR40")).thenReturn(Optional.of(predmet));
        PredmetResponse response = service.findBySifra("PR40");
        assertEquals("PR40", response.getSifra());
    }

    @Test
    void testGetProsecnaOcenaSuccess() {
        when(predmetRepository.findBySifra("PR40")).thenReturn(Optional.of(predmet));
        when(polozenPredmetRepository.getProsecnaOcenaZaPredmetIzmedju(predmet, 2020, 2024))
                .thenReturn(9.5);

        Double ocena = service.getProsecnaOcena("PR40", 2020, 2024);
        assertEquals(9.5, ocena);
    }
}
