package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.OstvareniPredispitniResponse;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.converters.OstvareniPredispitniConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OstvareniPredispitniServiceTest {

    @Mock
    private OstvareniPredispitniRepository ostvareniPredispitniRepository;

    @Mock
    private SlusaPredmetRepository slusaPredmetRepository;

    @Mock
    private PredispitneObavezeRepository predispitneObavezeRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @InjectMocks
    private OstvareniPredispitniService service;

    private OstvareniPredispitni entity;
    private OstvareniPredispitniRequest request;
    private SlusaPredmet slusaPredmet;
    private PredispitneObaveze predispitneObaveze;

    @BeforeEach
    void setUp() {
        // Kreiranje entiteta
        slusaPredmet = new SlusaPredmet();
        slusaPredmet.setId(1L);

        predispitneObaveze = new PredispitneObaveze();
        predispitneObaveze.setId(1L);
        predispitneObaveze.setMaksimalanBrojPoena(100);

        entity = new OstvareniPredispitni();
        entity.setId(1L);
        entity.setOstvareniPoeni(85);
        entity.setSlusaPredmet(slusaPredmet);
        entity.setPredispitneObaveze(predispitneObaveze);

        // DTO request
        PredispitneObavezeRequest poReq = new PredispitneObavezeRequest();
        poReq.setId(predispitneObaveze.getId());
        SlusaPredmetRequest spReq = new SlusaPredmetRequest(1L,"2025/2026","CS",123,3,"nastavnik@test.com","MAT101");

        request = new OstvareniPredispitniRequest(1L, 85, poReq, spReq);
    }

    @Test
    void testSave_Success() {
        when(slusaPredmetRepository.findById(slusaPredmet.getId())).thenReturn(Optional.of(slusaPredmet));
        when(predispitneObavezeRepository.findById(predispitneObaveze.getId())).thenReturn(Optional.of(predispitneObaveze));
        when(ostvareniPredispitniRepository.findBySlusaPredmet(slusaPredmet)).thenReturn(Arrays.asList());
        when(ostvareniPredispitniRepository.save(any(OstvareniPredispitni.class))).thenReturn(entity);

        OstvareniPredispitniResponse response = service.save(request);

        assertNotNull(response);
        assertEquals(85, response.getOstvareniPoeni());
        verify(ostvareniPredispitniRepository, times(1)).save(any(OstvareniPredispitni.class));
    }

    @Test
    void testSave_AlreadyExistsThrows() {
        when(slusaPredmetRepository.findById(slusaPredmet.getId())).thenReturn(Optional.of(slusaPredmet));
        when(predispitneObavezeRepository.findById(predispitneObaveze.getId())).thenReturn(Optional.of(predispitneObaveze));
        when(ostvareniPredispitniRepository.findBySlusaPredmet(slusaPredmet)).thenReturn(Arrays.asList(entity));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.save(request));
        assertEquals("Već postoji unos za ovu kombinaciju slusaPredmet + predispitna obaveza!", ex.getMessage());
    }

    @Test
    void testFindAll_Success() {
        when(ostvareniPredispitniRepository.findAll()).thenReturn(Arrays.asList(entity));
        List<OstvareniPredispitniResponse> list = service.findAll();
        assertEquals(1, list.size());
        assertEquals(85, list.get(0).getOstvareniPoeni());
    }

    @Test
    void testFindAll_EmptyThrows() {
        when(ostvareniPredispitniRepository.findAll()).thenReturn(Arrays.asList());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.findAll());
        assertEquals("Nema unosa!", ex.getMessage());
    }

    @Test
    void testFindById_Success() {
        when(ostvareniPredispitniRepository.findById(1L)).thenReturn(Optional.of(entity));
        OstvareniPredispitniResponse response = service.findById(1L);
        assertEquals(85, response.getOstvareniPoeni());
    }

    @Test
    void testFindById_NotFoundThrows() {
        when(ostvareniPredispitniRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.findById(1L));
        assertEquals("Ne postoji zapis sa ID: 1", ex.getMessage());
    }

    @Test
    void testUpdate_Success() {
        when(slusaPredmetRepository.findById(slusaPredmet.getId())).thenReturn(Optional.of(slusaPredmet));
        when(predispitneObavezeRepository.findById(predispitneObaveze.getId())).thenReturn(Optional.of(predispitneObaveze));
        when(ostvareniPredispitniRepository.findBySlusaPredmet(slusaPredmet)).thenReturn(Arrays.asList(entity));
        when(ostvareniPredispitniRepository.save(entity)).thenReturn(entity);

        request.setOstvareniPoeni(90);
        OstvareniPredispitniResponse response = service.update(request);

        assertEquals(90, response.getOstvareniPoeni());
        verify(ostvareniPredispitniRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_NotFoundThrows() {
        when(slusaPredmetRepository.findById(slusaPredmet.getId())).thenReturn(Optional.of(slusaPredmet));
        when(predispitneObavezeRepository.findById(predispitneObaveze.getId())).thenReturn(Optional.of(predispitneObaveze));
        when(ostvareniPredispitniRepository.findBySlusaPredmet(slusaPredmet)).thenReturn(Arrays.asList());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.update(request));
        assertEquals("Zapis ne postoji za update!", ex.getMessage());
    }

    @Test
    void testDelete_Success() {
        when(ostvareniPredispitniRepository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(ostvareniPredispitniRepository).deleteById(1L);
        service.delete(1L);
        verify(ostvareniPredispitniRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFoundThrows() {
        when(ostvareniPredispitniRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.delete(1L));
        assertEquals("Ne postoji zapis za ID: 1", ex.getMessage());
    }

    @Test
    void testGetOstvareniPoeniZaStudentaNaPredmetu_Success() {
        when(ostvareniPredispitniRepository.findOstvareniPoeniZaStudentaNaPredmetu(1L, 1L, "2025/2026"))
                .thenReturn(Arrays.asList(entity));

        List<OstvareniPredispitniResponse> list = service.getOstvareniPoeniZaStudentaNaPredmetu(1L,1L,"2025/2026");
        assertEquals(1, list.size());
        assertEquals(85, list.get(0).getOstvareniPoeni());
    }

    @Test
    void testGetOstvareniPoeniZaStudentaNaPredmetu_EmptyThrows() {
        when(ostvareniPredispitniRepository.findOstvareniPoeniZaStudentaNaPredmetu(1L, 1L, "2025/2026"))
                .thenReturn(Arrays.asList());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.getOstvareniPoeniZaStudentaNaPredmetu(1L,1L,"2025/2026"));
        assertEquals("Nema unosa za datog studenta i predmet u ovoj školskoj godini", ex.getMessage());
    }
}
