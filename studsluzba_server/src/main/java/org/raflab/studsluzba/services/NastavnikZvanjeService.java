package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.utils.converters.NastavnikConverter;
import org.raflab.studsluzba.utils.converters.NastavnikZvanjeConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NastavnikZvanjeService {
    private final NastavnikZvanjeRepository repository;
    private final NastavnikRepository nastavnikRepository;

    // ---------------- CREATE ----------------
    @Transactional
    public NastavnikZvanjeResponse save(NastavnikZvanjeRequest request) {
        Nastavnik nastavnik = nastavnikRepository.findByEmail(request.getNastavnikRequest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nastavnik sa email-om " + request.getNastavnikRequest().getEmail() + " ne postoji!"
                ));

        if (request.isAktivno()) {
            List<NastavnikZvanje> svaZvanja = repository.findAll();
            for (NastavnikZvanje z : svaZvanja) {
                if (z.getNastavnik().getEmail().equals(nastavnik.getEmail()) && z.isAktivno()) {
                    z.setAktivno(false);
                    repository.save(z);
                }
            }
        }

        NastavnikZvanje novoZvanje = new NastavnikZvanje();
        novoZvanje.setDatumIzbora(request.getDatumIzbora());
        novoZvanje.setNaucnaOblast(request.getNaucnaOblast());
        novoZvanje.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        novoZvanje.setZvanje(request.getZvanje());
        novoZvanje.setAktivno(request.isAktivno());
        novoZvanje.setNastavnik(nastavnik);

        NastavnikZvanje saved = repository.save(novoZvanje);
        return new NastavnikZvanjeResponse(
                saved.getDatumIzbora(),
                saved.getNaucnaOblast(),
                saved.getUzaNaucnaOblast(),
                saved.getZvanje(),
                saved.isAktivno(),
                NastavnikConverter.toNastavnikResponse(nastavnik)
        );
    }

    // ---------------- READ ----------------
    public List<NastavnikZvanjeResponse> findAll() {
        List<NastavnikZvanje> lista = repository.findAll();
        return lista.stream()
                .map(NastavnikZvanjeConverter::toNastavnikZvanjeResponse)
                .collect(Collectors.toList());
    }

    public NastavnikZvanjeResponse findById(Long id) {
        NastavnikZvanje nz = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zvanje sa ID " + id + " ne postoji!"));
        return NastavnikZvanjeConverter.toNastavnikZvanjeResponse(nz);
    }

    // ---------------- UPDATE ----------------
    @Transactional
    public NastavnikZvanjeResponse update(NastavnikZvanjeRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Za update zvanja je potreban ID zvanja.");
        }

        NastavnikZvanje zvanje = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Zvanje sa ID " + request.getId() + " ne postoji!"));

        Nastavnik nastavnik = nastavnikRepository.findByEmail(request.getNastavnikRequest().getEmail())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nastavnik sa email-om " + request.getNastavnikRequest().getEmail() + " ne postoji!"
                ));

        if (request.isAktivno()) {
            List<NastavnikZvanje> svaZvanja = repository.findAll();
            for (NastavnikZvanje z : svaZvanja) {
                if (!z.getId().equals(zvanje.getId())
                        && z.getNastavnik().getEmail().equals(nastavnik.getEmail())
                        && z.isAktivno()) {
                    z.setAktivno(false);
                    repository.save(z);
                }
            }
        }

        zvanje.setDatumIzbora(request.getDatumIzbora());
        zvanje.setNaucnaOblast(request.getNaucnaOblast());
        zvanje.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        zvanje.setZvanje(request.getZvanje());
        zvanje.setAktivno(request.isAktivno());
        zvanje.setNastavnik(nastavnik);

        NastavnikZvanje updated = repository.save(zvanje);
        return new NastavnikZvanjeResponse(
                updated.getDatumIzbora(),
                updated.getNaucnaOblast(),
                updated.getUzaNaucnaOblast(),
                updated.getZvanje(),
                updated.isAktivno(),
                NastavnikConverter.toNastavnikResponse(nastavnik)
        );
    }

    // ---------------- DELETE ----------------
    @Transactional
    public void delete(Long id, Long nastavnikId, java.time.LocalDate datumIzbora) {
        if (id != null) {
            if (!repository.existsById(id)) {
                throw new IllegalArgumentException("Zvanje sa ID " + id + " ne postoji!");
            }
            repository.deleteById(id);
        } else if (nastavnikId != null && datumIzbora != null) {
            NastavnikZvanje nz = repository.findAll().stream()
                    .filter(z -> z.getNastavnik().getId().equals(nastavnikId) &&
                            z.getDatumIzbora().equals(datumIzbora))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Zvanje za ovog nastavnika i datum ne postoji!"));
            repository.delete(nz);
        } else {
            throw new IllegalArgumentException("Moraš proslediti ID ili kombinaciju nastavnik+datum.");
        }
    }
}
