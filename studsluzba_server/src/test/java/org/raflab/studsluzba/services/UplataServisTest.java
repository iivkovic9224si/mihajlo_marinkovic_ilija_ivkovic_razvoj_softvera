package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.Uplata;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.UplataRepository;
import org.raflab.studsluzba.utils.converters.UplataConverter;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UplataServisTest {

    @InjectMocks
    private UplataService service;

    @Mock
    private UplataRepository uplataRepository;
    @Mock
    private StudentPodaciRepository studentPodaciRepository;
    @Mock
    private SkolskaGodinaRepository skolskaGodinaRepository;

    private Uplata uplata;
    private UplataRequest request;
    private StudentPodaci studentPodaci;
    private SkolskaGodina skolskaGodina;

    @BeforeEach
    void setUp() {
        studentPodaci = new StudentPodaci();
        studentPodaci.setJmbg("1234567890123");

        skolskaGodina = new SkolskaGodina();
        skolskaGodina.setGodina("2025/2026");

        uplata = new Uplata();
        uplata.setId(1L);
        uplata.setDatumUplate(LocalDate.of(2025,1,1));
        uplata.setIznosUDinarima(360000.0);
        uplata.setStudentPodaci(studentPodaci);
        uplata.setSkolskaGodina(skolskaGodina);
        uplata.setSrednjiKurs(120.0);

        request = new UplataRequest();
        request.setDatumUplate(LocalDate.of(2025,1,1));
        request.setIznosUDinarima(360000.0);
        request.setStudentJmbg(studentPodaci.getJmbg());
        request.setSkolskaGodina(skolskaGodina.getGodina());
    }

    @Test
    void testDodajUplatuSuccess() {
        when(studentPodaciRepository.getStudentPodaciByJmbg(anyString())).thenReturn(studentPodaci);
        when(skolskaGodinaRepository.findByGodina(anyString())).thenReturn(skolskaGodina);
        when(uplataRepository.save(any())).thenReturn(uplata);

        Uplata saved = service.dodajUplatu(request);

        assertNotNull(saved);
        assertEquals(uplata.getId(), saved.getId());
        verify(uplataRepository, times(1)).save(any());
    }

    @Test
    void testOstaloZaUplatuSuccess() {
        when(uplataRepository.uplaceno(any())).thenReturn(1000.0);

        UplataResponse response = service.ostaloZaUplatu(studentPodaci);

        assertNotNull(response);
        assertEquals(2000.0, response.getIznosUEurima());
        assertTrue(response.getIznosUDinarima() > 0);
    }

    @Test
    void testOstaloZaUplatuNullUplaceno() {
        when(uplataRepository.uplaceno(any())).thenReturn(null);

        UplataResponse response = service.ostaloZaUplatu(studentPodaci);

        assertEquals(3000.0, response.getIznosUEurima());
        assertTrue(response.getIznosUDinarima() > 0);
    }

    @Test
    void testFindAllSuccess() {
        when(uplataRepository.findAll()).thenReturn(Collections.singletonList(uplata));
        when(uplataRepository.uplaceno(any())).thenReturn(1000.0);

        List<UplataResponse> responses = service.findAll();

        assertEquals(1, responses.size());
    }

    @Test
    void testFindByIdSuccess() {
        when(uplataRepository.findById(1L)).thenReturn(Optional.of(uplata));
        when(uplataRepository.uplaceno(any())).thenReturn(1000.0);

        UplataResponse response = service.findById(1L);

        assertNotNull(response);
    }

    @Test
    void testFindByIdNotFoundThrows() {
        when(uplataRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.findById(99L));
        assertTrue(ex.getMessage().contains("nije pronađena"));
    }

    @Test
    void testUpdateSuccess() {
        when(uplataRepository.findById(1L)).thenReturn(Optional.of(uplata));
        when(studentPodaciRepository.getStudentPodaciByJmbg(anyString())).thenReturn(studentPodaci);
        when(skolskaGodinaRepository.findByGodina(anyString())).thenReturn(skolskaGodina);
        when(uplataRepository.save(any())).thenReturn(uplata);
        when(uplataRepository.uplaceno(any())).thenReturn(1000.0);

        UplataResponse response = service.update(1L, request);

        assertNotNull(response);
        verify(uplataRepository, times(1)).save(any());
    }

    @Test
    void testUpdateNotFoundThrows() {
        when(uplataRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(99L, request));
        assertTrue(ex.getMessage().contains("nije pronađena"));
    }

    @Test
    void testDeleteSuccess() {
        when(uplataRepository.findById(1L)).thenReturn(Optional.of(uplata));

        service.delete(1L);

        verify(uplataRepository, times(1)).delete(uplata);
    }

    @Test
    void testDeleteNotFoundThrows() {
        when(uplataRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(99L));
        assertTrue(ex.getMessage().contains("nije pronađena"));
    }
}
