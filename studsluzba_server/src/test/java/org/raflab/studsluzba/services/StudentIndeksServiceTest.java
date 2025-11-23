package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentIndeksServiceTest {

    @InjectMocks
    private StudentIndeksService service;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private StudentPodaciRepository studentPodaciRepository;

    @Mock
    private StudijskiProgramRepository studijskiProgramRepository;

    @Mock
    private StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;

    @Mock
    private SrednjaSkolaRepository srednjaSkolaRepository;

    private StudentIndeks studentIndeks;
    private StudentIndeksRequest request;
    private StudentPodaci studentPodaci;
    private StudijskiProgram studijskiProgram;
    private SrednjaSkola srednjaSkola;

    @BeforeEach
    void setUp() {
        studentPodaci = new StudentPodaci();
        studentPodaci.setId(1L);
        studentPodaci.setIme("Petar");
        studentPodaci.setPrezime("Petrovic");

        studijskiProgram = new StudijskiProgram();
        studijskiProgram.setId(1L);
        studijskiProgram.setOznaka("RI");

        studentIndeks = new StudentIndeks();
        studentIndeks.setId(1L);
        studentIndeks.setBroj(1);
        studentIndeks.setGodina(2025);
        studentIndeks.setStudProgramOznaka("RI");
        studentIndeks.setStudent(studentPodaci);
        studentIndeks.setStudijskiProgram(studijskiProgram);
        studentIndeks.setAktivan(true);
        studentIndeks.setVaziOd(LocalDate.now());

        request = new StudentIndeksRequest();
        request.setGodina(2025);
        request.setStudProgramOznaka("RI");
        request.setBroj(1);
        request.setAktivan(true);
        request.setStudentId(1L);

        srednjaSkola = new SrednjaSkola();
        srednjaSkola.setId(1L);
        srednjaSkola.setSifra("SS123");
        srednjaSkola.setNaziv("Srednja Skola Petar Petrovic");
    }

    @Test
    void testSaveSuccess() {
        when(studentPodaciRepository.findById(1L)).thenReturn(Optional.of(studentPodaci));
        when(studijskiProgramRepository.findByOznaka("RI")).thenReturn(Arrays.asList(studijskiProgram));
        when(studentIndeksRepository.save(any(StudentIndeks.class))).thenReturn(studentIndeks);

        Long id = service.saveIndeks(request);

        assertNotNull(id);
        assertEquals(1L, id);
    }

    @Test
    void testSaveStudentNotFoundThrows() {
        when(studentPodaciRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> service.saveIndeks(request));
        assertTrue(exception.getMessage().contains("nije pronađen"));
    }

    @Test
    void testFindByStudentIdAndAktivanSuccess() {
        when(studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(1L)).thenReturn(studentIndeks);

        StudentIndeks result = service.findByStudentIdAndAktivan(1L);

        assertNotNull(result);
        assertEquals(1, result.getBroj());
    }

    @Test
    void testUpdateSuccess() {
        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(studentIndeks));
        when(studentIndeksRepository.save(any(StudentIndeks.class))).thenReturn(studentIndeks);

        Long id = service.update(1L, request);

        assertNotNull(id);
        assertEquals(1L, id);
        verify(studentIndeksRepository, times(1)).save(studentIndeks);
    }

    @Test
    void testUpdateNonExistentThrows() {
        when(studentIndeksRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> service.update(99L, request));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testDeleteByStudentIndeksSuccess() {
        when(studentIndeksRepository.findStudentIndeks("RI", 1, 2025)).thenReturn(studentIndeks);
        doNothing().when(studentIndeksRepository).delete(studentIndeks);

        service.deleteByStudentIndeks("RI", 1, 2025);

        verify(studentIndeksRepository, times(1)).delete(studentIndeks);
    }

    @Test
    void testDeleteByStudentIndeksNonExistentThrows() {
        when(studentIndeksRepository.findStudentIndeks("RI", 99, 2025)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> service.deleteByStudentIndeks("RI", 99, 2025));
        assertTrue(exception.getMessage().contains("nije pronađen"));
    }

    @Test
    void testFindBrojNextAvailable() {
        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(2025, "RI"))
                .thenReturn(Arrays.asList(1, 2, 4));

        int next = service.findBroj(2025, "RI");

        assertEquals(3, next); // 3 je prvi slobodan broj
    }

    @Test
    void testGetUpisaneGodineByStudentIndeksSuccess() {
        when(studentIndeksRepository.findStudentIndeks("RI", 2025, 1)).thenReturn(studentIndeks);
        when(studijskiTokAktivnostiRepository.getUpisaneGodineByStudentIndeks(studentIndeks))
                .thenReturn(Arrays.asList(2025, 2026));

        List<Integer> godine = service.getUpisaneGodineByStudentIndeks(request);

        assertNotNull(godine);
        assertEquals(2, godine.size());
        assertTrue(godine.contains(2025));
        assertTrue(godine.contains(2026));
    }

    @Test
    void testGetUpisaneGodineByStudentIndeksNotFoundThrows() {
        when(studentIndeksRepository.findStudentIndeks("RI", 2025, 1)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> service.getUpisaneGodineByStudentIndeks(request));
        assertTrue(exception.getMessage().contains("nije pronađen"));
    }

    @Test
    void testGetObnovljeneGodineSuccess() {
        when(studentIndeksRepository.findStudentIndeks("RI", 2025, 1)).thenReturn(studentIndeks);
        when(studijskiTokAktivnostiRepository.getObnovljeneGodineByStudentIndeks(studentIndeks))
                .thenReturn(Arrays.asList(2026));

        List<Integer> godine = service.getObnovljeneGodine(request);
        assertEquals(1, godine.size());
        assertEquals(2026, godine.get(0));
    }

    @Test
    void testGetStudentPodaciByStudentIndeksSuccess() {
        when(studentIndeksRepository.findStudentIndeks("RI", 1, 2025)).thenReturn(studentIndeks);
        when(studentPodaciRepository.getStudentPodaciFromStudentIndeks(studentIndeks))
                .thenReturn(studentPodaci);

        StudentPodaciResponse response = service.getStudentPodaciByStudentIndeks("RI", 1, 2025);
        assertNotNull(response);
        assertEquals("Petar", response.getIme());
    }


    @Test
    void testGetStudentPodaciBySrednjaSkolaSuccess() {
        when(srednjaSkolaRepository.findBySifra("SS123")).thenReturn(Optional.of(srednjaSkola));
        when(studentPodaciRepository.getStudentiBySrednjaSkola(srednjaSkola))
                .thenReturn(Arrays.asList(studentPodaci));

        List<StudentPodaciResponse> studenti = service.getStudentPodaciBySrednjaSkola("SS123");
        assertEquals(1, studenti.size());
        assertEquals("Petar", studenti.get(0).getIme());
    }

    @Test
    void testGetStudentPodaciBySrednjaSkolaNotFoundThrows() {
        when(srednjaSkolaRepository.findBySifra("SS999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> service.getStudentPodaciBySrednjaSkola("SS999"));
        assertTrue(exception.getMessage().contains("nije pronadjena"));
    }
}
