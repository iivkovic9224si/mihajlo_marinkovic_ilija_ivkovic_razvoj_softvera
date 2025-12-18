package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.VisokoSkolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoSkolskaUstanovaResponse;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisokoSkolskaUstanovaServiceTest {

    @InjectMocks
    private VisokoSkolskaUstanovaService service;

    @Mock
    private VisokoSkolskaUstanovaRepository repository;

    private VisokoSkolskaUstanova ustanova;
    private VisokoSkolskaUstanovaRequest request;

    @BeforeEach
    void setUp() {
        ustanova = new VisokoSkolskaUstanova();
        ustanova.setId(1L);
        ustanova.setSifra("VSU123");
        ustanova.setNaziv("Univerzitet Test");
        ustanova.setVrsta(VrstaVisokoSkolskihUstanova.UNIVERZITET);

        request = new VisokoSkolskaUstanovaRequest();
        request.setSifra("VSU123");
        request.setNaziv("Univerzitet Test");
        request.setVrsta(VrstaVisokoSkolskihUstanova.UNIVERZITET);
    }

    @Test
    void testSaveSuccess() {
        when(repository.findBySifra("VSU123")).thenReturn(Optional.empty());
        when(repository.save(any(VisokoSkolskaUstanova.class))).thenReturn(ustanova);

        VisokoSkolskaUstanovaResponse response = service.save(request);

        assertNotNull(response);
        assertEquals("VSU123", response.getSifra());
        assertEquals("Univerzitet Test", response.getNaziv());
        assertEquals(VrstaVisokoSkolskihUstanova.UNIVERZITET, response.getVrsta());
    }

    @Test
    void testSaveDuplicateSifraThrows() {
        when(repository.findBySifra("VSU123")).thenReturn(Optional.of(ustanova));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertTrue(exception.getMessage().contains("već postoji"));
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(ustanova));

        List<VisokoSkolskaUstanovaResponse> list = service.findAll();

        assertEquals(1, list.size());
        assertEquals("VSU123", list.get(0).getSifra());
    }

    @Test
    void testFindBySifraSuccess() {
        when(repository.findBySifra("VSU123")).thenReturn(Optional.of(ustanova));

        VisokoSkolskaUstanovaResponse response = service.findBySifra("VSU123");

        assertNotNull(response);
        assertEquals("VSU123", response.getSifra());
    }

    @Test
    void testFindBySifraNotFoundThrows() {
        when(repository.findBySifra("VSU999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findBySifra("VSU999"));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testUpdateSuccess() {
        VisokoSkolskaUstanovaRequest noviPodaci = new VisokoSkolskaUstanovaRequest();
        noviPodaci.setSifra("VSU123");
        noviPodaci.setNaziv("Novi Univerzitet");
        noviPodaci.setVrsta(VrstaVisokoSkolskihUstanova.FAKULTET);

        when(repository.findBySifra("VSU123")).thenReturn(Optional.of(ustanova));
        when(repository.save(any(VisokoSkolskaUstanova.class))).thenReturn(ustanova);

        VisokoSkolskaUstanovaResponse response = service.update("VSU123", noviPodaci);

        assertEquals("VSU123", response.getSifra());
        assertEquals("Novi Univerzitet", response.getNaziv());
        assertEquals(VrstaVisokoSkolskihUstanova.FAKULTET, response.getVrsta());
    }

    @Test
    void testUpdateNonExistentThrows() {
        VisokoSkolskaUstanovaRequest noviPodaci = new VisokoSkolskaUstanovaRequest();
        noviPodaci.setSifra("VSU999");
        noviPodaci.setNaziv("Novi");
        noviPodaci.setVrsta(VrstaVisokoSkolskihUstanova.FAKULTET);

        when(repository.findBySifra("VSU999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update("VSU999", noviPodaci));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }

    @Test
    void testDeleteSuccess() {
        when(repository.findBySifra("VSU123")).thenReturn(Optional.of(ustanova));
        doNothing().when(repository).delete(ustanova);

        service.delete("VSU123");

        verify(repository, times(1)).delete(ustanova);
    }

    @Test
    void testDeleteNonExistentThrows() {
        when(repository.findBySifra("VSU999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.delete("VSU999"));
        assertTrue(exception.getMessage().contains("ne postoji"));
    }
}
