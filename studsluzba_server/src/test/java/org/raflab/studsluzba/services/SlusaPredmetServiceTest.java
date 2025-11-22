package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlusaPredmetServiceTest {

    @InjectMocks
    private SlusaPredmetService service;

    @Mock
    private SlusaPredmetRepository slusaPredmetRepository;

    @Mock
    private SkolskaGodinaRepository skolskaGodinaRepository;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private DrziPredmetRepository drziPredmetRepository;

    private SkolskaGodina skolskaGodina;
    private StudentIndeks student;
    private DrziPredmet drziPredmet;
    private SlusaPredmet slusaPredmet;

    @BeforeEach
    void setUp() {
        skolskaGodina = new SkolskaGodina();
        skolskaGodina.setId(1L);
        skolskaGodina.setGodina("2025/2026");
        skolskaGodina.setAktivna(true);

        student = new StudentIndeks();
        student.setId(1L);
        student.setStudProgramOznaka("RA");
        student.setBroj(123);
        student.setGodina(2025);

        drziPredmet = new DrziPredmet();
        drziPredmet.setId(1L);

        slusaPredmet = new SlusaPredmet();
        slusaPredmet.setId(1L);
        slusaPredmet.setSkolskaGodina(skolskaGodina);
        slusaPredmet.setStudentIndeks(student);
        slusaPredmet.setDrziPredmet(drziPredmet);
    }

    @Test
    void testSaveSuccess() {
        SlusaPredmetRequest request = new SlusaPredmetRequest(0, "2025/2026", "RA", 123, 2025, "nastavnik@mail.com", "SIF123");

        when(skolskaGodinaRepository.findByGodina("2025/2026")).thenReturn(skolskaGodina);
        when(studentIndeksRepository.findStudentIndeks("RA", 2025, 123)).thenReturn(student);
        when(drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra("nastavnik@mail.com", "SIF123")).thenReturn(drziPredmet);
        when(slusaPredmetRepository.save(any(SlusaPredmet.class))).thenReturn(slusaPredmet);

        SlusaPredmetResponse response = service.save(request);

        assertNotNull(response);
        assertEquals("2025/2026", response.getSkolskaGodina());
    }

    @Test
    void testSaveSkolskaGodinaNotFoundThrows() {
        SlusaPredmetRequest request = new SlusaPredmetRequest(0, "2030/2031", "RA", 123, 2025, "nastavnik@mail.com", "SIF123");
        when(skolskaGodinaRepository.findByGodina("2030/2031")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertTrue(exception.getMessage().contains("Skolska godina 2030/2031 ne postoji."));
    }

    @Test
    void testUpdateSuccess() {
        SlusaPredmetRequest request = new SlusaPredmetRequest(1, "2025/2026", "RA", 123, 2025, "nastavnik@mail.com", "SIF123");

        when(skolskaGodinaRepository.findByGodina("2025/2026")).thenReturn(skolskaGodina);
        when(drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra("nastavnik@mail.com", "SIF123")).thenReturn(drziPredmet);

        doNothing().when(slusaPredmetRepository).updateSlusaPredmet(request.getId(), skolskaGodina.getId(), drziPredmet.getId());

        service.update(request);

        verify(slusaPredmetRepository, times(1)).updateSlusaPredmet(request.getId(), skolskaGodina.getId(), drziPredmet.getId());
    }

    @Test
    void testDeleteSuccess() {
        SlusaPredmetRequest request = new SlusaPredmetRequest(1, null, null, 0, 0, null, null);
        doNothing().when(slusaPredmetRepository).deleteById(1L);

        service.delete(request);

        verify(slusaPredmetRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByIdSuccess() {
        when(slusaPredmetRepository.findById(1L)).thenReturn(Optional.of(slusaPredmet));

        Optional<SlusaPredmetResponse> response = service.findById(1L);

        assertTrue(response.isPresent());
        assertEquals("2025/2026", response.get().getSkolskaGodina());
    }

    @Test
    void testFindAllSuccess() {
        when(slusaPredmetRepository.findAll()).thenReturn(Arrays.asList(slusaPredmet));

        List<SlusaPredmetResponse> responses = service.findAll();

        assertEquals(1, responses.size());
        assertEquals("2025/2026", responses.get(0).getSkolskaGodina());
    }

    @Test
    void testGetSlusaPredmetiZaStudentaSuccess() {
        when(studentIndeksRepository.findStudentIndeks("RA", 2025, 123)).thenReturn(student);
        when(slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(student.getId())).thenReturn(Arrays.asList(slusaPredmet));

        List<SlusaPredmetResponse> responses = service.getSlusaPredmetiZaStudenta("RA", 2025, 123);

        assertEquals(1, responses.size());
        assertEquals("2025/2026", responses.get(0).getSkolskaGodina());
    }

    @Test
    void testGetSlusaPredmetiZaStudentaNotFoundThrows() {
        when(studentIndeksRepository.findStudentIndeks("RA", 2025, 123)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.getSlusaPredmetiZaStudenta("RA", 2025, 123));
        assertTrue(exception.getMessage().contains("Student sa indeksom RA123/2025 ne postoji."));
    }
}
