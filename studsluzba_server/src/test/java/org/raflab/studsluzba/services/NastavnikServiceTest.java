package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NastavnikServiceTest {

    @Mock
    private NastavnikRepository nastavnikRepository;

    @Mock
    private DrziPredmetRepository drziPredmetRepository;

    @Mock
    private VisokoSkolskaUstanovaRepository ustanovaRepository;

    @Mock
    private NastavnikZvanjeRepository nastavnikZvanjeRepository;

    @InjectMocks
    private NastavnikService nastavnikService;

    private Nastavnik nastavnik;
    private NastavnikRequest request;

    @BeforeEach
    void setUp() {
        nastavnik = new Nastavnik();
        nastavnik.setId(1L);
        nastavnik.setIme("Mihajlo");
        nastavnik.setPrezime("Marinkovic");
        nastavnik.setSrednjeIme("Ilija");
        nastavnik.setEmail("mihajlo@example.com");
        nastavnik.setDatumRodjenja(LocalDate.of(1995, 5, 24));
        nastavnik.setPol('M');
        nastavnik.setJmbg("1234567890123");

        request = new NastavnikRequest();
        request.setIme("Mihajlo");
        request.setPrezime("Marinkovic");
        request.setSrednjeIme("Ilija");
        request.setEmail("mihajlo@example.com");
        request.setDatumRodjenja(LocalDate.of(1995, 5, 24));
        request.setPol('M');
        request.setJmbg("1234567890123");
    }

    @Test
    void testSaveSuccess() {
        when(nastavnikRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(nastavnikRepository.findAll()).thenReturn(new ArrayList<>());
        when(nastavnikRepository.save(any(Nastavnik.class))).thenReturn(nastavnik);

        NastavnikResponse response = nastavnikService.save(request);

        assertNotNull(response);
        assertEquals(nastavnik.getId(), response.getId());
        assertEquals(nastavnik.getEmail(), response.getEmail());

        verify(nastavnikRepository, times(1)).save(any(Nastavnik.class));
    }

    @Test
    void testSaveDuplicateEmailThrowsException() {
        when(nastavnikRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(nastavnik));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            nastavnikService.save(request);
        });

        assertTrue(ex.getMessage().contains("već postoji"));
    }

    @Test
    void testFindAll() {
        List<Nastavnik> lista = new ArrayList<>();
        lista.add(nastavnik);
        when(nastavnikRepository.findAll()).thenReturn(lista);

        List<NastavnikResponse> responses = nastavnikService.findAll();
        assertEquals(1, responses.size());
        assertEquals("Mihajlo", responses.get(0).getIme());
    }

    @Test
    void testFindById() {
        when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(nastavnik));

        NastavnikResponse response = nastavnikService.findById(1L);
        assertEquals("Mihajlo", response.getIme());
    }

    @Test
    void testUpdateSuccess() {
        request.setId(1L);
        when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(nastavnik));
        when(nastavnikRepository.save(any(Nastavnik.class))).thenReturn(nastavnik);

        NastavnikResponse response = nastavnikService.update(request);
        assertEquals("Mihajlo", response.getIme());
        verify(nastavnikRepository, times(1)).save(any(Nastavnik.class));
    }
}
