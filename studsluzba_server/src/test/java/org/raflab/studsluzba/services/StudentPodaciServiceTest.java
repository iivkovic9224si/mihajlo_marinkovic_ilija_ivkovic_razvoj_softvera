package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentPodaciServiceTest {

    @InjectMocks
    private StudentPodaciService service;

    @Mock
    private StudentPodaciRepository studentPodaciRepository;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private SlusaPredmetRepository slusaPredmetRepository;

    private StudentPodaci studentPodaci;
    private StudentPodaciRequest request;
    private StudentIndeks studentIndeks;

    @BeforeEach
    void setUp() {
        studentPodaci = new StudentPodaci();
        studentPodaci.setId(1L);
        studentPodaci.setIme("Petar");
        studentPodaci.setPrezime("Petrovic");
        studentPodaci.setSrednjeIme("Marko");
        studentPodaci.setJmbg("1234567890123");
        studentPodaci.setDatumRodjenja(LocalDate.of(2000,1,1));
        studentPodaci.setMestoPrebivalista("Beograd");
        studentPodaci.setDrzavljanstvo("RS");
        studentPodaci.setPol('M');
        studentPodaci.setAdresa("Adresa 1");
        studentPodaci.setEmail("petar@mail.com");

        request = new StudentPodaciRequest();
        request.setIme("Petar");
        request.setPrezime("Petrovic");
        request.setSrednjeIme("Marko");
        request.setJmbg("1234567890123");
        request.setDatumRodjenja(LocalDate.of(2000,1,1));
        request.setMestoPrebivalista("Beograd");
        request.setDrzavljanstvo("RS");
        request.setPol('M');
        request.setAdresa("Adresa 1");
        request.setEmail("petar@mail.com");

        studentIndeks = new StudentIndeks();
        studentIndeks.setId(1L);
        studentIndeks.setStudent(studentPodaci);
    }

    @Test
    void testSaveSuccess() {
        when(studentPodaciRepository.save(any(StudentPodaci.class))).thenReturn(studentPodaci);

        StudentPodaci saved = service.save(request);

        assertNotNull(saved);
        assertEquals("Petar", saved.getIme());
        verify(studentPodaciRepository, times(1)).save(any(StudentPodaci.class));
    }

    @Test
    void testFindAllSuccess() {
        when(studentPodaciRepository.findAll()).thenReturn(Arrays.asList(studentPodaci));

        List<StudentPodaciResponse> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Petar", result.get(0).getIme());
    }

    @Test
    void testFindByIdSuccess() {
        when(studentPodaciRepository.findById(1L)).thenReturn(Optional.of(studentPodaci));

        Optional<StudentPodaciResponse> response = service.findById(1L);

        assertTrue(response.isPresent());
        assertEquals("Petar", response.get().getIme());
    }

    @Test
    void testFindByIdNotFound() {
        when(studentPodaciRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StudentPodaciResponse> response = service.findById(1L);

        assertNull(response);
    }

    @Test
    void testFindByJmbgSuccess() {
        when(studentPodaciRepository.findByJmbg("1234567890123")).thenReturn(Optional.of(studentPodaci));

        Optional<StudentPodaciResponse> response = service.findByJmbg("1234567890123");

        assertTrue(response.isPresent());
        assertEquals("Petar", response.get().getIme());
    }

    @Test
    void testFindByJmbgNotFound() {
        when(studentPodaciRepository.findByJmbg("0000000000000")).thenReturn(Optional.empty());

        Optional<StudentPodaciResponse> response = service.findByJmbg("0000000000000");

        assertNull(response);
    }

    @Test
    void testUpdateSuccess() {
        when(studentPodaciRepository.findById(1L)).thenReturn(Optional.of(studentPodaci));
        when(studentPodaciRepository.save(any(StudentPodaci.class))).thenReturn(studentPodaci);

        Long id = service.update(1L, request);

        assertEquals(1L, id);
        verify(studentPodaciRepository, times(1)).save(studentPodaci);
    }

    @Test
    void testUpdateNotFoundThrows() {
        when(studentPodaciRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.update(99L, request));
        assertTrue(ex.getMessage().contains("ne postoji"));
    }

    @Test
    void testDeleteStudentPodaciSuccess() {
        when(studentPodaciRepository.findByJmbg("1234567890123")).thenReturn(Optional.of(studentPodaci));
        doNothing().when(studentPodaciRepository).delete(studentPodaci);

        service.deleteStudentPodaci(request);

        verify(studentPodaciRepository, times(1)).delete(studentPodaci);
    }

    @Test
    void testDeleteStudentPodaciNotFoundThrows() {
        StudentPodaciRequest missingRequest = new StudentPodaciRequest();
        missingRequest.setJmbg("0000000000000");

        when(studentPodaciRepository.findByJmbg("0000000000000")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteStudentPodaci(missingRequest));

        assertTrue(ex.getMessage().contains("nije pronađen"));
    }

    @Test
    void testFindAllPageable() {
        StudentPodaci student = new StudentPodaci();
        student.setId(1L);
        student.setIme("Petar");

        Page<StudentPodaci> page = new PageImpl<>(Collections.singletonList(student));
        when(studentPodaciRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        Page<StudentPodaciResponse> result = service.findAll(PageRequest.of(0, 10, Sort.by("ime")));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Petar", result.getContent().get(0).getIme());
    }

    @Test
    void testGetStudentProfileSuccess() {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setId(1L);

        StudentIndeks indeks = new StudentIndeks();
        indeks.setId(1L);
        indeks.setStudent(studentPodaci);

        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(indeks));
        when(slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(1L))
                .thenReturn(Collections.emptyList());

        var profile = service.getStudentProfile(1L);

        assertNotNull(profile);
        assertEquals(indeks, profile.getIndeks());
        assertTrue(profile.getSlusaPredmete().isEmpty());
    }

    @Test
    void testGetStudentProfileIndexNotFoundThrows() {
        when(studentIndeksRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getStudentProfile(99L));
    }

    @Test
    void testGetStudentWebProfileSuccess() {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setId(1L);

        StudentIndeks indeks = new StudentIndeks();
        indeks.setId(1L);
        indeks.setStudent(studentPodaci);

        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(indeks));
        when(studentPodaciRepository.getAktivanIndeks(1L)).thenReturn(indeks);
        when(slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(1L))
                .thenReturn(Collections.emptyList());

        var profile = service.getStudentWebProfile(1L);

        assertNotNull(profile);
        assertEquals(indeks, profile.getAktivanIndeks());
        assertTrue(profile.getSlusaPredmete().isEmpty());
    }

    @Test
    void testGetStudentWebProfileIndexNotFoundThrows() {
        when(studentIndeksRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getStudentWebProfile(99L));
    }
}
