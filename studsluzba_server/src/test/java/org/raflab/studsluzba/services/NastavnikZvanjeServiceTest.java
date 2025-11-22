package org.raflab.studsluzba.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NastavnikZvanjeServiceTest {

    @Mock
    private NastavnikZvanjeRepository zvanjeRepository;

    @Mock
    private NastavnikRepository nastavnikRepository;

    @InjectMocks
    private NastavnikZvanjeService service;

    private Nastavnik nastavnik;
    private NastavnikZvanje zvanje;

    @BeforeEach
    void setUp() {
        // Kreiranje nastavnika
        nastavnik = new Nastavnik();
        nastavnik.setId(1L);
        nastavnik.setEmail("test@nastavnik.com");
        nastavnik.setIme("Test");
        nastavnik.setPrezime("Profesor");
        nastavnik.setSrednjeIme("Srednje");
        nastavnik.setJmbg("1234567890123");

        // Kreiranje zvanja
        zvanje = new NastavnikZvanje();
        zvanje.setId(1L);
        zvanje.setDatumIzbora(LocalDate.of(2025, 1, 1));
        zvanje.setNaucnaOblast("Matematika");
        zvanje.setUzaNaucnaOblast("Algebra");
        zvanje.setZvanje("Docent");
        zvanje.setAktivno(true);
        zvanje.setNastavnik(nastavnik);
    }

    @Test
    void testSave() {
        // Priprema DTO-a za request
        NastavnikRequest nr = new NastavnikRequest();
        nr.setEmail(nastavnik.getEmail());
        nr.setIme(nastavnik.getIme());
        nr.setPrezime(nastavnik.getPrezime());
        nr.setSrednjeIme(nastavnik.getSrednjeIme());
        nr.setJmbg(nastavnik.getJmbg());

        NastavnikZvanjeRequest request = new NastavnikZvanjeRequest();
        request.setDatumIzbora(LocalDate.of(2025, 1, 1));
        request.setNaucnaOblast("Matematika");
        request.setUzaNaucnaOblast("Algebra");
        request.setZvanje("Docent");
        request.setAktivno(true);
        request.setNastavnikRequest(nr);

        when(nastavnikRepository.findByEmail(nastavnik.getEmail())).thenReturn(Optional.of(nastavnik));
        when(zvanjeRepository.findAll()).thenReturn(Arrays.asList());
        when(zvanjeRepository.save(any(NastavnikZvanje.class))).thenReturn(zvanje);

        NastavnikZvanjeResponse response = service.save(request);

        assertNotNull(response);
        assertEquals("Matematika", response.getNaucnaOblast());
        assertEquals("Docent", response.getZvanje());
        assertTrue(response.isAktivno());

        verify(zvanjeRepository, times(1)).save(any(NastavnikZvanje.class));
    }

    @Test
    void testFindAll() {
        when(zvanjeRepository.findAll()).thenReturn(Arrays.asList(zvanje));

        List<NastavnikZvanjeResponse> list = service.findAll();

        assertEquals(1, list.size());
        assertEquals("Docent", list.get(0).getZvanje());
    }

    @Test
    void testFindById() {
        when(zvanjeRepository.findById(1L)).thenReturn(Optional.of(zvanje));

        NastavnikZvanjeResponse response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Docent", response.getZvanje());
    }

    @Test
    void testUpdate() {
        // DTO za update
        NastavnikRequest nr = new NastavnikRequest();
        nr.setEmail(nastavnik.getEmail());
        nr.setIme(nastavnik.getIme());
        nr.setPrezime(nastavnik.getPrezime());
        nr.setSrednjeIme(nastavnik.getSrednjeIme());
        nr.setJmbg(nastavnik.getJmbg());

        NastavnikZvanjeRequest request = new NastavnikZvanjeRequest();
        request.setId(1L);
        request.setDatumIzbora(LocalDate.of(2025, 2, 1));
        request.setNaucnaOblast("Fizika");
        request.setUzaNaucnaOblast("Mehanika");
        request.setZvanje("Profesor");
        request.setAktivno(true);
        request.setNastavnikRequest(nr);

        when(zvanjeRepository.findById(1L)).thenReturn(Optional.of(zvanje));
        when(nastavnikRepository.findByEmail(nastavnik.getEmail())).thenReturn(Optional.of(nastavnik));
        when(zvanjeRepository.findAll()).thenReturn(Arrays.asList(zvanje));
        when(zvanjeRepository.save(any(NastavnikZvanje.class))).thenReturn(zvanje);

        NastavnikZvanjeResponse response = service.update(request);

        assertNotNull(response);
        assertEquals("Fizika", response.getNaucnaOblast());
        assertEquals("Profesor", response.getZvanje());
    }

    @Test
    void testDeleteById() {
        when(zvanjeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(zvanjeRepository).deleteById(1L);

        service.delete(1L, null, null);

        verify(zvanjeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByNastavnikIdAndDatum() {
        when(zvanjeRepository.findAll()).thenReturn(Arrays.asList(zvanje));
        doNothing().when(zvanjeRepository).delete(zvanje);

        service.delete(null, nastavnik.getId(), LocalDate.of(2025, 1, 1));

        verify(zvanjeRepository, times(1)).delete(zvanje);
    }

    @Test
    void testDeleteNoParamsThrows() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.delete(null, null, null);
        });

        assertEquals("Moraš proslediti ID ili kombinaciju nastavnik+datum.", exception.getMessage());
    }
}
