package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrijavaIspitaServiceTest {

    @InjectMocks
    private PrijavaIspitaService service;

    @Mock
    private PrijavaIspitaRepository prijavaIspitaRepository;

    @Mock
    private IspitRepository ispitRepository;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private SlusaPredmetRepository slusaPredmetRepository;

    private Ispit ispit;
    private StudentIndeks student;
    private SlusaPredmet slusaPredmet;
    private PrijavaIspitaRequest request;

    @BeforeEach
    void setUp() {
        DrziPredmet drziPredmet = new DrziPredmet();

        ispit = new Ispit();
        ispit.setId(1L);

        student = new StudentIndeks();
        student.setId(1L);

        slusaPredmet = new SlusaPredmet();
        slusaPredmet.setStudentIndeks(student);

        request = new PrijavaIspitaRequest(
                1L,
                LocalDate.now(),
                1L,
                2025,
                123,
                "RA"
        );
    }

    @Test
    void testSaveSuccess() {
        PrijavaIspita savedPrijava = new PrijavaIspita();
        savedPrijava.setIspit(ispit);
        savedPrijava.setSlusaPredmet(slusaPredmet);

        when(ispitRepository.findById(anyLong())).thenReturn(Optional.of(ispit));
        when(studentIndeksRepository.findByGodinaAndBrojAndStudProgramOznaka(anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(student));
        when(slusaPredmetRepository.findByStudentIndeksAndDrziPredmet(anyLong(), anyLong()))
                .thenReturn(Optional.of(slusaPredmet));
        when(prijavaIspitaRepository.save(any(PrijavaIspita.class))).thenReturn(savedPrijava);

        PrijavaIspitaResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(ispit.getId(), response.getIspitResponse().getId());
    }

    @Test
    void testSaveSlusaPredmetNotFoundThrows() {
        when(ispitRepository.findById(anyLong())).thenReturn(Optional.of(ispit));
        when(studentIndeksRepository.findByGodinaAndBrojAndStudProgramOznaka(anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(student));
        when(slusaPredmetRepository.findByStudentIndeksAndDrziPredmet(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.save(request));
        assertEquals("SlusaPredmet nije pronađen", exception.getMessage());
    }

    @Test
    void testFindStudentiByIspitId() {
        when(prijavaIspitaRepository.findSlusaPredmetByIspitId(anyLong()))
                .thenReturn(List.of(slusaPredmet));

        List<StudentIndeksResponse> result = service.findStudentiByIspitId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(student.getId(), result.get(0).getId());
    }
}
