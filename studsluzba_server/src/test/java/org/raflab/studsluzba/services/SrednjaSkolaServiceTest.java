package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.entity.SrednjaSkola;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SrednjaSkolaServiceTest {

    @InjectMocks
    private SrednjaSkolaService service;

    @Mock
    private SrednjaSkolaRepository repository;

    private SrednjaSkola skola;
    private SrednjaSkolaRequest request;

    @BeforeEach
    void setUp() {
        skola = new SrednjaSkola();
        skola.setId(1L);
        skola.setSifra("SK123");
        skola.setNaziv("Gimnazija Test");
        skola.setVrstaSrednjeSkole(VrstaSrednjeSkole.GIMNAZIJA);

        request = new SrednjaSkolaRequest("SK123", "Gimnazija Test", VrstaSrednjeSkole.GIMNAZIJA);
    }

    @Test
    void testSaveSuccess() {
        when(repository.findBySifra("SK123")).thenReturn(Optional.empty());
        when(repository.save(any(SrednjaSkola.class))).thenReturn(skola);

        SrednjaSkolaResponse response = service.save(request);

        assertNotNull(response);
        assertEquals("SK123", response.getSifra());
        assertEquals("Gimnazija Test", response.getNaziv());
        assertEquals(VrstaSrednjeSkole.GIMNAZIJA, response.getVrstaSrednjeSkole());
    }

    @Test
    void testSaveDuplicateSifraThrows() {
        when(repository.findBySifra("SK123")).thenReturn(Optional.of(skola));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertTrue(exception.getMessage().contains("već postoji"));
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(skola));

        List<SrednjaSkolaResponse> skole = service.findAll();

        assertEquals(1, skole.size());
        assertEquals("SK123", skole.get(0).getSifra());
    }

    @Test
    void testFindBySifraSuccess() {
        when(repository.findBySifra("SK123")).thenReturn(Optional.of(skola));

        SrednjaSkolaResponse response = service.findBySifra("SK123");

        assertNotNull(response);
        assertEquals("SK123", response.getSifra());
    }

    @Test
    void testFindBySifraNotFoundThrows() {
        when(repository.findBySifra("SK999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findBySifra("SK999"));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testUpdateSuccess() {
        SrednjaSkolaRequest noviPodaci = new SrednjaSkolaRequest("SK123", "Nova Gimnazija", VrstaSrednjeSkole.STRUCNA);

        when(repository.findBySifra("SK123")).thenReturn(Optional.of(skola));
        when(repository.save(any(SrednjaSkola.class))).thenReturn(skola);

        SrednjaSkolaResponse response = service.update("SK123", noviPodaci);

        assertEquals("SK123", response.getSifra());
        assertEquals("Nova Gimnazija", response.getNaziv());
        assertEquals(VrstaSrednjeSkole.STRUCNA, response.getVrstaSrednjeSkole());
    }

    @Test
    void testUpdateNonExistentThrows() {
        SrednjaSkolaRequest noviPodaci = new SrednjaSkolaRequest("SK999", "Nova", VrstaSrednjeSkole.STRUCNA);
        when(repository.findBySifra("SK999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update("SK999", noviPodaci));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testDeleteSuccess() {
        when(repository.findBySifra("SK123")).thenReturn(Optional.of(skola));
        doNothing().when(repository).delete(skola);

        service.delete("SK123");

        verify(repository, times(1)).delete(skola);
    }

    @Test
    void testDeleteNonExistentThrows() {
        when(repository.findBySifra("SK999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.delete("SK999"));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }
}
