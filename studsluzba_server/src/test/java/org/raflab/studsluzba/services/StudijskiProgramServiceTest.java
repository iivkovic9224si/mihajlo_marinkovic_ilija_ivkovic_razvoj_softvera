package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.StudijskiProgramNewRequest;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.converters.PredmetConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudijskiProgramServiceTest {

    @InjectMocks
    private StudProgramService service;

    @Mock
    private StudijskiProgramRepository repository;

    private StudijskiProgram sp;
    private StudijskiProgramNewRequest newRequest;
    private StudijskiProgramRequest request;

    @BeforeEach
    void setUp() {
        sp = new StudijskiProgram();
        sp.setId(1L);
        sp.setOznaka("RN");
        sp.setNaziv("Računarske nauke");
        sp.setGodinaAkreditacije(2020);
        sp.setZvanje("Dipl. ing.");
        sp.setTrajanjeGodina(4);
        sp.setTrajanjeSemestara(8);
        sp.setVrstaStudija("OAS");
        sp.setUkupnoEspb(240);

        newRequest = new StudijskiProgramNewRequest();
        newRequest.setOznaka("RN");
        newRequest.setNaziv("Računarske nauke");
        newRequest.setGodinaAkreditacije(2020);
        newRequest.setZvanje("Dipl. ing.");
        newRequest.setTrajanjeGodina(4);
        newRequest.setTrajanjeSemestara(8);
        newRequest.setVrstaStudija("OAS");
        newRequest.setUkupnoEspb(240);

        request = new StudijskiProgramRequest();
        request.setOznaka("RN");
        request.setNaziv("Računarske nauke");
        request.setGodinaAkreditacije(2020);
        request.setZvanje("Dipl. ing.");
        request.setTrajanjeGodina(4);
        request.setTrajanjeSemestara(8);
        request.setVrstaStudija("OAS");
        request.setUkupnoEspb(240);
    }

    @Test
    void testSave() {
        when(repository.save(any(StudijskiProgram.class))).thenReturn(sp);

        Long id = service.save(newRequest);

        assertEquals(1L, id);
        verify(repository, times(1)).save(any(StudijskiProgram.class));
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(sp));

        List<StudijskiProgramResponse> list = service.findAll();

        assertEquals(1, list.size());
        assertEquals("RN", list.get(0).getOznaka());
    }

    @Test
    void testFindByIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(sp));

        Optional<StudijskiProgramResponse> response = service.findById(1L);

        assertTrue(response.isPresent());
        assertEquals("RN", response.get().getOznaka());
    }

    @Test
    void testFindByIdNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<StudijskiProgramResponse> response = service.findById(99L);
        assertFalse(response.isPresent());
    }

    @Test
    void testFindAllOznake() {
        when(repository.findAllOznaka()).thenReturn(Arrays.asList("RN", "RM"));

        List<String> oznake = service.findAllOznake();

        assertEquals(2, oznake.size());
        assertTrue(oznake.contains("RN"));
    }

    @Test
    void testDeleteStudijskiProgramExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deleteStudijskiProgram(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudijskiProgramNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteStudijskiProgram(99L));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testGetAllSortedByGodinaDesc() {
        when(repository.getAllSortedByGodinaDesc()).thenReturn(Arrays.asList(sp));

        List<StudijskiProgramResponse> list = service.getAllSortedByGodinaDesc();

        assertEquals(1, list.size());
        assertEquals(2020, list.get(0).getGodinaAkreditacije());
    }

    @Test
    void testUpdateSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(sp));
        when(repository.save(any(StudijskiProgram.class))).thenReturn(sp);

        StudijskiProgramResponse updated = service.update(1L, newRequest);

        assertEquals("RN", updated.getOznaka());
        assertEquals("Računarske nauke", updated.getNaziv());
        verify(repository, times(1)).save(any(StudijskiProgram.class));
    }

    @Test
    void testUpdateNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.update(99L, newRequest));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testFindPredmetByStudProgramSuccess() {
        when(repository.findByOznaka("RN")).thenReturn(Arrays.asList(sp));
        Predmet p1 = new Predmet();
        p1.setId(1L);
        p1.setNaziv("Matematika");
        when(repository.findPredmetByStudProgram(sp)).thenReturn(Arrays.asList(p1));

        List<PredmetResponse> list = service.findPredmetByStudProgram(request);

        assertEquals(1, list.size());
        assertEquals("Matematika", list.get(0).getNaziv());
    }

    @Test
    void testFindPredmetByStudProgramNotExists() {
        when(repository.findByOznaka("RN")).thenReturn(Arrays.asList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.findPredmetByStudProgram(request));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }
}
