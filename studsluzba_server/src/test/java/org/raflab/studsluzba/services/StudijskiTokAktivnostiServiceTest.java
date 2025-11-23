package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudijskiTokAktivnostiServiceTest {

    @InjectMocks
    private StudijskiTokAktivnostiService service;

    @Mock
    private StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;
    @Mock
    private StudentIndeksRepository studentIndeksRepository;
    @Mock
    private DrziPredmetRepository drziPredmetRepository;
    @Mock
    private SlusaPredmetRepository slusaPredmetRepository;
    @Mock
    private SkolskaGodinaRepository skolskaGodinaRepository;

    private StudijskiTokAktivnosti studijskiTok;
    private StudijskiTokAktivnostiRequest request;
    private StudentIndeks studentIndeks;

    @BeforeEach
    void setUp() {
        studentIndeks = new StudentIndeks();
        studentIndeks.setId(1L);

        studijskiTok = new StudijskiTokAktivnosti();
        studijskiTok.setId(1L);
        studijskiTok.setTip(VrstaStudijskeAktivnosti.UPIS);
        studijskiTok.setGodina(1);
        studijskiTok.setDatum(LocalDate.of(2025,1,1));
        studijskiTok.setNapomena("Napomena");
        studijskiTok.setStudentIndeks(studentIndeks);

        request = new StudijskiTokAktivnostiRequest(
                "UPIS", 1, LocalDate.of(2025,1,1),
                "Napomena", true, 1, 1, "SI"
        );
    }

    @Test
    void testSaveSuccess() {
        when(studijskiTokAktivnostiRepository.save(any(StudijskiTokAktivnosti.class)))
                .thenReturn(studijskiTok);

        StudijskiTokAktivnosti saved = service.save(studijskiTok);

        assertNotNull(saved);
        assertEquals(1, saved.getId());
        verify(studijskiTokAktivnostiRepository, times(1)).save(any());
    }

    @Test
    void testFindAllSuccess() {
        StudijskiProgram studijskiProgram = new StudijskiProgram();
        studijskiProgram.setOznaka("SP1");

        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setStudijskiProgram(studijskiProgram);
        studentIndeks.setBroj(123);
        studentIndeks.setGodina(1);

        StudijskiTokAktivnosti studijskiTok = new StudijskiTokAktivnosti();
        studijskiTok.setId(1L);
        studijskiTok.setTip(VrstaStudijskeAktivnosti.UPIS);
        studijskiTok.setGodina(2025);
        studijskiTok.setDatum(LocalDate.now());
        studijskiTok.setStudentIndeks(studentIndeks);

        when(studijskiTokAktivnostiRepository.findAll())
                .thenReturn(Collections.singletonList(studijskiTok));

        List<StudijskiTokAktivnostiResponse> result = service.findAll();

        assertEquals(1, result.size());
    }


    @Test
    void testFindByIdSuccess() {
        StudijskiProgram studijskiProgram = new StudijskiProgram();
        studijskiProgram.setOznaka("SP1");

        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setStudijskiProgram(studijskiProgram);
        studentIndeks.setBroj(123);
        studentIndeks.setGodina(1);

        StudijskiTokAktivnosti studijskiTok = new StudijskiTokAktivnosti();
        studijskiTok.setId(1L);
        studijskiTok.setTip(VrstaStudijskeAktivnosti.UPIS);
        studijskiTok.setGodina(2025);
        studijskiTok.setDatum(LocalDate.now());
        studijskiTok.setStudentIndeks(studentIndeks);

        when(studijskiTokAktivnostiRepository.findById(1L))
                .thenReturn(Optional.of(studijskiTok));

        Optional<StudijskiTokAktivnostiResponse> response = service.findById(1L);

        assertTrue(response.isPresent());
    }


    @Test
    void testFindByIdNotFound() {
        when(studijskiTokAktivnostiRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<StudijskiTokAktivnostiResponse> response = service.findById(99L);

        assertTrue(response.isEmpty());
    }

    @Test
    void testUpdateSuccess() {
        when(studijskiTokAktivnostiRepository.findById(1L))
                .thenReturn(Optional.of(studijskiTok));
        when(studijskiTokAktivnostiRepository.save(any())).thenReturn(studijskiTok);

        StudijskiTokAktivnosti updated = service.update(studijskiTok);

        assertNotNull(updated);
        verify(studijskiTokAktivnostiRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNotFoundThrows() {
        studijskiTok.setId(99L);
        when(studijskiTokAktivnostiRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(studijskiTok));
        assertTrue(ex.getMessage().contains("ne postoji"));
    }


    @Test
    void testUpisiStudentaNaGodinuSuccess() {
        when(studentIndeksRepository.findStudentIndeks(anyString(), anyInt(), anyInt()))
                .thenReturn(studentIndeks);
        when(drziPredmetRepository.getDrziPredmetByNastavnikMailAndPredmetSifra(anyString(), anyString()))
                .thenReturn(new DrziPredmet());
        when(skolskaGodinaRepository.findByAktivnaTrue()).thenReturn(new SkolskaGodina());

        service.upisiStudentaNaGodinu(request, Collections.singletonList(new DrziPredmetRequest("2025/2026","prof@mail.com", "SI")));

        verify(studijskiTokAktivnostiRepository, times(1)).save(any());
        verify(slusaPredmetRepository, times(1)).save(any());
    }

    @Test
    void testUpisiStudentaNaGodinuStudentNotFoundThrows() {
        when(studentIndeksRepository.findStudentIndeks(anyString(), anyInt(), anyInt())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.upisiStudentaNaGodinu(request, Collections.emptyList()));
        assertTrue(ex.getMessage().contains("nije pronadjen"));
    }

    @Test
    void testDeleteSuccess() {
        StudijskiTokAktivnosti sta = new StudijskiTokAktivnosti();

        when(service.findByRequest(request)).thenReturn(sta);

        service.delete(request);
        verify(studijskiTokAktivnostiRepository, times(1)).delete(sta);
    }


    @Test
    void testDeleteNotFoundThrows() {
        when(service.findByRequest(request)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(request));
        assertTrue(ex.getMessage().contains("nije pronađena"));
    }
}
