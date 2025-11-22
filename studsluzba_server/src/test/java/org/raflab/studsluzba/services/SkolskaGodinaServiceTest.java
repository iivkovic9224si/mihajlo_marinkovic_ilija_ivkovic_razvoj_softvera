package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkolskaGodinaServiceTest {

    @InjectMocks
    private SkolskaGodinaService service;

    @Mock
    private SkolskaGodinaRepository repository;

    private SkolskaGodina godina1;
    private SkolskaGodina godina2;

    @BeforeEach
    void setUp() {
        godina1 = new SkolskaGodina();
        godina1.setId(1L);
        godina1.setGodina("2024/2025");
        godina1.setAktivna(true);

        godina2 = new SkolskaGodina();
        godina2.setId(2L);
        godina2.setGodina("2025/2026");
        godina2.setAktivna(false);
    }

    @Test
    void testSaveSuccess() throws Exception {
        SkolskaGodinaRequest request = new SkolskaGodinaRequest(true, "2026/2027");

        doNothing().when(repository).deactivateAll();
        when(repository.save(any(SkolskaGodina.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SkolskaGodinaResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(request.getGodina(), response.getGodina());
        assertTrue(response.isAktivna());

        verify(repository, times(1)).deactivateAll();
        verify(repository, times(1)).save(any(SkolskaGodina.class));
    }

    @Test
    void testSaveEmptyGodinaThrows() {
        SkolskaGodinaRequest request = new SkolskaGodinaRequest(true, "");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertEquals("Godina ne sme biti prazna.", exception.getMessage());
    }

    @Test
    void testFindAllSuccess() {
        when(repository.findAll()).thenReturn(Arrays.asList(godina1, godina2));

        List<SkolskaGodinaResponse> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getGodina().equals("2024/2025")));
        assertTrue(result.stream().anyMatch(r -> r.getGodina().equals("2025/2026")));
    }

    @Test
    void testFindAllEmptyThrows() {
        when(repository.findAll()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findAll());
        assertEquals("Nema školskih godina u bazi.", exception.getMessage());
    }

    @Test
    void testFindByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(godina1));

        SkolskaGodinaResponse response = service.findById(1L);

        assertEquals("2024/2025", response.getGodina());
        assertTrue(response.isAktivna());
    }

    @Test
    void testFindByIdNotFoundThrows() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findById(3L));
        assertEquals("Školska godina sa ID 3 ne postoji.", exception.getMessage());
    }

    @Test
    void testUpdateSuccess() {
        SkolskaGodinaRequest request = new SkolskaGodinaRequest(true, "2025/2026");

        when(repository.findByGodina("2025/2026")).thenReturn(godina2);
        when(repository.save(any(SkolskaGodina.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repository.findByAktivnaTrue()).thenReturn(godina2);

        SkolskaGodinaResponse response = service.update(request);

        assertEquals("2025/2026", response.getGodina());
        assertTrue(response.isAktivna());

        verify(repository, times(1)).save(godina2);
    }

    @Test
    void testUpdateNonExistingThrows() {
        SkolskaGodinaRequest request = new SkolskaGodinaRequest(true, "2030/2031");

        when(repository.findByGodina("2030/2031")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update(request));
        assertEquals("Školska godina '2030/2031' ne postoji.", exception.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistingThrows() {
        when(repository.existsById(3L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.delete(3L));
        assertEquals("Školska godina sa ID 3 ne postoji!", exception.getMessage());
    }

    @Test
    void testFindActiveYearSuccess() {
        when(repository.findByAktivnaTrue()).thenReturn(godina1);

        SkolskaGodinaResponse response = service.findActiveYear();

        assertEquals("2024/2025", response.getGodina());
        assertTrue(response.isAktivna());
    }

    @Test
    void testFindActiveYearNotFoundThrows() {
        when(repository.findByAktivnaTrue()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findActiveYear());
        assertEquals("Trenutno nema aktivne školske godine.", exception.getMessage());
    }
}
