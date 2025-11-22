package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IspitniRokServiceTest {

    @Mock
    private IspitniRokRepository ispitniRokRepository;

    @Mock
    private SkolskaGodinaRepository skolskaGodinaRepository;

    @InjectMocks
    private IspitniRokService ispitniRokService;

    private SkolskaGodina aktivnaGodina;
    private IspitniRok rok;

    @BeforeEach
    void setUp() {
        aktivnaGodina = new SkolskaGodina();
        aktivnaGodina.setGodina("2025/26");
        aktivnaGodina.setAktivna(true);

        rok = new IspitniRok();
        rok.setId(1L);
        rok.setPocetak(LocalDate.of(2025, 10, 1));
        rok.setKraj(LocalDate.of(2025, 12, 31));
        rok.setSkolskaGodina(aktivnaGodina);
    }

    @Test
    void testSave_success() {
        IspitniRokRequest request = new IspitniRokRequest();
        request.setPocetak(rok.getPocetak());
        request.setKraj(rok.getKraj());
        request.setGodina("2025/26");

        when(skolskaGodinaRepository.findByGodina("2025/26")).thenReturn(aktivnaGodina);
        when(ispitniRokRepository.save(any(IspitniRok.class))).thenReturn(rok);

        IspitniRokResponse response = ispitniRokService.save(request);

        assertNotNull(response);
        assertEquals(rok.getId(), response.getId());
        assertEquals(rok.getPocetak(), response.getPocetak());
        assertEquals(rok.getKraj(), response.getKraj());
        assertEquals(aktivnaGodina.getGodina(), response.getGodina());
    }

    @Test
    void testFindAll() {
        List<IspitniRok> lista = new ArrayList<>();
        lista.add(rok);
        when(ispitniRokRepository.findAll()).thenReturn(lista);

        List<IspitniRokResponse> result = ispitniRokService.findAll();
        assertEquals(1, result.size());
        assertEquals(rok.getId(), result.get(0).getId());
    }

    @Test
    void testFindAllAktivnaGodina() {
        List<IspitniRok> lista = new ArrayList<>();
        lista.add(rok);

        when(skolskaGodinaRepository.findByAktivnaTrue()).thenReturn(aktivnaGodina);
        when(ispitniRokRepository.findBySkolskaGodina(aktivnaGodina)).thenReturn(lista);

        List<IspitniRokResponse> result = ispitniRokService.findAllAktivnaGodina();
        assertEquals(1, result.size());
        assertEquals(rok.getId(), result.get(0).getId());
    }

    @Test
    void testFindById() {
        when(ispitniRokRepository.findById(1L)).thenReturn(Optional.of(rok));

        IspitniRokResponse response = ispitniRokService.findById(1L);
        assertNotNull(response);
        assertEquals(rok.getId(), response.getId());
    }

    @Test
    void testUpdate_success() {
        IspitniRokRequest request = new IspitniRokRequest();
        request.setId(1L);
        request.setPocetak(rok.getPocetak());
        request.setKraj(rok.getKraj());
        request.setGodina("2025/26");

        when(skolskaGodinaRepository.findByGodina("2025/26")).thenReturn(aktivnaGodina);
        when(ispitniRokRepository.save(any(IspitniRok.class))).thenReturn(rok);

        IspitniRokResponse response = ispitniRokService.update(request);
        assertNotNull(response);
        assertEquals(rok.getId(), response.getId());
    }

    @Test
    void testDelete_success() {
        when(ispitniRokRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ispitniRokRepository).deleteById(1L);

        ispitniRokService.delete(1L);
        verify(ispitniRokRepository, times(1)).deleteById(1L);
    }
}
