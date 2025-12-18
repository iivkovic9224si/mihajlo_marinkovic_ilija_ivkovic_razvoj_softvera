package org.raflab.studsluzba.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.repositories.PredispitneObavezeRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.PredispitneObavezeService;
import org.raflab.studsluzba.services.PredmetService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredispitneObavezeServiceTest {

    @Mock
    private PredispitneObavezeRepository predispitneObavezeRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private PredmetService predmetService;

    @InjectMocks
    private PredispitneObavezeService service;

    private Predmet predmet;
    private PredispitneObavezeRequest request;

    @BeforeEach
    void setUp() {
        predmet = new Predmet();
        predmet.setSifra("PR40");
        predmet.setNaziv("Matematika");
        predmet.setEspb(6);
        predmet.setObavezan(true);

        request = new PredispitneObavezeRequest();
        request.setMaksimalanBrojPoena(50);
        request.setVrstaPredispitneObaveze(VrstaPredispitneObaveze.KOLOKVIJUM);
        request.setPredmetRequest(new org.raflab.studsluzba.controllers.request.PredmetRequest());
        request.getPredmetRequest().setSifra(predmet.getSifra());
    }

    @Test
    void testSaveSuccess() {
        when(predmetRepository.findBySifra(predmet.getSifra())).thenReturn(Optional.of(predmet));
        when(predispitneObavezeRepository.findByPredmet(predmet)).thenReturn(List.of());
        when(predispitneObavezeRepository.save(any(PredispitneObaveze.class))).thenAnswer(i -> i.getArguments()[0]);

        PredispitneObavezeResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(request.getMaksimalanBrojPoena(), response.getMaksimalanBrojPoena());
        assertEquals(request.getVrstaPredispitneObaveze(), response.getVrstaPredispitneObaveze());

        verify(predispitneObavezeRepository, times(1)).save(any(PredispitneObaveze.class));
    }

    @Test
    void testSaveDuplicateThrows() {
        PredispitneObaveze existing = new PredispitneObaveze();
        existing.setVrstaPredispitneObaveze(request.getVrstaPredispitneObaveze());
        existing.setMaksimalanBrojPoena(request.getMaksimalanBrojPoena());
        existing.setPredmet(predmet);

        when(predmetRepository.findBySifra(predmet.getSifra())).thenReturn(Optional.of(predmet));
        when(predispitneObavezeRepository.findByPredmet(predmet)).thenReturn(List.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertTrue(ex.getMessage().contains("već postoji"));
    }

    @Test
    void testFindAllSuccess() {
        PredispitneObaveze po = new PredispitneObaveze();
        po.setId(1L);
        po.setMaksimalanBrojPoena(50);
        po.setVrstaPredispitneObaveze(VrstaPredispitneObaveze.KOLOKVIJUM);
        po.setPredmet(predmet);

        when(predispitneObavezeRepository.findAll()).thenReturn(List.of(po));

        List<PredispitneObavezeResponse> list = service.findAll();
        assertEquals(1, list.size());
    }

    @Test
    void testFindByIdNotFound() {
        when(predispitneObavezeRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.findById(1L));
        assertTrue(ex.getMessage().contains("ne postoji"));
    }

    @Test
    void testUpdateSuccess() {
        PredispitneObaveze entity = new PredispitneObaveze();
        entity.setId(1L);
        entity.setMaksimalanBrojPoena(40);
        entity.setVrstaPredispitneObaveze(VrstaPredispitneObaveze.KOLOKVIJUM);
        entity.setPredmet(predmet);

        request.setId(1L);
        request.setMaksimalanBrojPoena(60);

        when(predispitneObavezeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(predmetRepository.findBySifra(predmet.getSifra())).thenReturn(Optional.of(predmet));
        when(predispitneObavezeRepository.save(any(PredispitneObaveze.class))).thenAnswer(i -> i.getArguments()[0]);

        PredispitneObavezeResponse updated = service.update(request);
        assertEquals(60, updated.getMaksimalanBrojPoena());
    }

    @Test
    void testDeleteSuccess() {
        PredispitneObaveze entity = new PredispitneObaveze();
        entity.setId(1L);

        when(predispitneObavezeRepository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(predispitneObavezeRepository, times(1)).deleteById(1L);
    }
}
