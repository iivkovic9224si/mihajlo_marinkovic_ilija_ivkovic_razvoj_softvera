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
import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.controllers.response.GrupaResponse;
import org.raflab.studsluzba.model.entity.Grupa;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.GrupaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupaServiceTest {

    @Mock
    private GrupaRepository grupaRepository;
    @Mock
    private StudijskiProgramRepository studijskiProgramRepository;
    @Mock
    private PredmetRepository predmetRepository;

    @InjectMocks
    private GrupaService grupaService;

    @Captor
    private ArgumentCaptor<Grupa> grupaCaptor;

    private StudijskiProgram sp;
    private Predmet p1;
    private Predmet p2;

    @BeforeEach
    void setup() {
        sp = new StudijskiProgram();
        sp.setId(1L);
        sp.setOznaka("SP1");

        p1 = new Predmet();
        p1.setId(1L);
        p1.setSifra("MAT101");

        p2 = new Predmet();
        p2.setId(2L);
        p2.setSifra("FIZ101");
    }

    @Test
    @DisplayName("Create Grupa success")
    void testSave() {
        GrupaRequest request = new GrupaRequest();
        request.setStudijskiProgramOznaka(sp.getOznaka());
        request.setSifrePredmeta(List.of(p1.getSifra(), p2.getSifra()));

        when(studijskiProgramRepository.findByOznakaSingle(sp.getOznaka())).thenReturn(sp);
        when(predmetRepository.findBySifra(p1.getSifra())).thenReturn(Optional.of(p1));
        when(predmetRepository.findBySifra(p2.getSifra())).thenReturn(Optional.of(p2));

        Grupa saved = new Grupa();
        saved.setId(1L);
        saved.setStudijskiProgram(sp);
        saved.setPredmeti(List.of(p1, p2));

        when(grupaRepository.save(any(Grupa.class))).thenReturn(saved);

        GrupaResponse response = grupaService.save(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(sp.getOznaka(), response.getStudijskiProgram().getOznaka());
        assertEquals(2, response.getPredmetResponses().size());

        verify(grupaRepository).save(grupaCaptor.capture());
        Grupa captured = grupaCaptor.getValue();
        assertEquals(sp.getOznaka(), captured.getStudijskiProgram().getOznaka());
        assertEquals(2, captured.getPredmeti().size());
    }

    @Test
    @DisplayName("Find all grupe")
    void testFindAll() {
        Grupa g = new Grupa();
        g.setId(1L);
        g.setStudijskiProgram(sp);
        g.setPredmeti(List.of(p1, p2));

        when(grupaRepository.findAll()).thenReturn(List.of(g));

        List<GrupaResponse> responses = grupaService.findAll();

        assertEquals(1, responses.size());
        assertEquals(sp.getOznaka(), responses.get(0).getStudijskiProgram().getOznaka());
        assertEquals(2, responses.get(0).getPredmetResponses().size());
    }

    @Test
    @DisplayName("Find grupa by ID")
    void testFindById() {
        Grupa g = new Grupa();
        g.setId(1L);
        g.setStudijskiProgram(sp);
        g.setPredmeti(List.of(p1, p2));

        when(grupaRepository.findByIdWithPredmeti(1L)).thenReturn(Optional.of(g));

        GrupaResponse response = grupaService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals(sp.getOznaka(), response.getStudijskiProgram().getOznaka());
        assertEquals(2, response.getPredmetResponses().size());
    }

    @Test
    @DisplayName("Update Grupa")
    void testUpdate() {
        Grupa g = new Grupa();
        g.setId(1L);
        g.setStudijskiProgram(sp);
        g.setPredmeti(List.of(p1));

        when(grupaRepository.findById(1L)).thenReturn(Optional.of(g));
        when(studijskiProgramRepository.findByOznakaSingle(sp.getOznaka())).thenReturn(sp);
        when(predmetRepository.findBySifra(p1.getSifra())).thenReturn(Optional.of(p1));
        when(predmetRepository.findBySifra(p2.getSifra())).thenReturn(Optional.of(p2));
        when(grupaRepository.save(any(Grupa.class))).thenReturn(g);

        GrupaRequest request = new GrupaRequest();
        request.setStudijskiProgramOznaka(sp.getOznaka());
        request.setSifrePredmeta(List.of(p1.getSifra(), p2.getSifra()));

        GrupaResponse response = grupaService.update(1L, request);

        assertEquals(sp.getOznaka(), response.getStudijskiProgram().getOznaka());
        assertEquals(2, response.getPredmetResponses().size());

        verify(grupaRepository).save(grupaCaptor.capture());
        Grupa captured = grupaCaptor.getValue();
        assertEquals(2, captured.getPredmeti().size());
    }

    @Test
    @DisplayName("Delete Grupa")
    void testDelete() {
        when(grupaRepository.existsById(1L)).thenReturn(true);

        grupaService.delete(1L);

        verify(grupaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete non-existing Grupa throws")
    void testDeleteNonExisting() {
        when(grupaRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> grupaService.delete(1L));
    }
}
