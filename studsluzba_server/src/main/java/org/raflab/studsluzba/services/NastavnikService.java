package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.repositories.VisokoSkolskaUstanovaRepository;
import org.raflab.studsluzba.utils.converters.DrziPredmetConverter;
import org.raflab.studsluzba.utils.converters.NastavnikConverter;
import org.raflab.studsluzba.utils.converters.VisokoSkolskaUstanovaConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.NastavnikRequest;
import response.DrziPredmetResponse;
import response.NastavnikResponse;
import response.NastavnikZvanjeResponse;
import response.VisokoSkolskaUstanovaResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NastavnikService {
    private final NastavnikRepository nastavnikRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final VisokoSkolskaUstanovaRepository ustanovaRepository;
    private final NastavnikZvanjeRepository nastavnikZvanjeRepository;

    // ----------------- CREATE -----------------
    @Transactional
    public NastavnikResponse save(NastavnikRequest request) {
        Optional<Nastavnik> byEmail = nastavnikRepository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            throw new IllegalArgumentException("Nastavnik sa email-om " + request.getEmail() + " već postoji!");
        }

        List<Nastavnik> svi = nastavnikRepository.findAll();
        for (Nastavnik n : svi) {
            if (n.getJmbg().equals(request.getJmbg())) {
                throw new IllegalArgumentException("Nastavnik sa JMBG-om " + request.getJmbg() + " već postoji!");
            }
        }

        Nastavnik nastavnik = NastavnikConverter.toNastavnik(request);
        Nastavnik saved = nastavnikRepository.save(nastavnik);
        return NastavnikConverter.toNastavnikResponse(saved);
    }

    // ----------------- READ -----------------
    public List<NastavnikResponse> findAll() {
        List<Nastavnik> lista = nastavnikRepository.findAll();
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Nema nastavnika u bazi!");
        }
        return NastavnikConverter.toNastavnikResponseList(lista);
    }

    public NastavnikResponse findById(Long id) {
        Optional<Nastavnik> opt = nastavnikRepository.findById(id);
        Nastavnik n = opt.orElseThrow(() -> new IllegalArgumentException("Nastavnik sa ID " + id + " ne postoji!"));
        return NastavnikConverter.toNastavnikResponse(n);
    }

    public NastavnikResponse findByEmail(String email) {
        Optional<Nastavnik> opt = nastavnikRepository.findByEmail(email);
        Nastavnik n = opt.orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));
        return NastavnikConverter.toNastavnikResponse(n);
    }

    // ----------------- UPDATE -----------------
    @Transactional
    public NastavnikResponse update(NastavnikRequest request) {
        Nastavnik nastavnik;
        if (request.getId() != null) {
            nastavnik = nastavnikRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa ID " + request.getId() + " ne postoji!"));
        } else if (request.getEmail() != null) {
            nastavnik = nastavnikRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + request.getEmail() + " ne postoji!"));
        } else {
            throw new IllegalArgumentException("Za ažuriranje mora biti prosleđen ID ili email.");
        }

        // PROVERA NOVOG EMAIL-A
        if (request.getEmail() != null && !request.getEmail().equals(nastavnik.getEmail())) {
            if (nastavnikRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email " + request.getEmail() + " već postoji!");
            }
            nastavnik.setEmail(request.getEmail());
        }

        nastavnik.setIme(request.getIme());
        nastavnik.setPrezime(request.getPrezime());
        nastavnik.setSrednjeIme(request.getSrednjeIme());
        nastavnik.setBrojTelefona(request.getBrojTelefona());
        nastavnik.setAdresa(request.getAdresa());
        nastavnik.setDatumRodjenja(request.getDatumRodjenja());
        nastavnik.setPol(request.getPol());
        nastavnik.setJmbg(request.getJmbg());

        Nastavnik updated = nastavnikRepository.save(nastavnik);
        return NastavnikConverter.toNastavnikResponse(updated);
    }


    // ----------------- DELETE -----------------
    @Transactional
    public void delete(Long id, String email) {
        if (id != null) {
            if (!nastavnikRepository.existsById(id)) {
                throw new IllegalArgumentException("Nastavnik sa ID " + id + " ne postoji!");
            }
            nastavnikRepository.deleteById(id);
        } else if (email != null) {
            Nastavnik n = nastavnikRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));
            nastavnikRepository.delete(n);
        } else {
            throw new IllegalArgumentException("Moraš proslediti ID ili email za brisanje.");
        }
    }

    // ----------------- DODATNI UPITI -----------------
    @Transactional(readOnly = true)
    public List<NastavnikZvanjeResponse> getZvanja(String email) {
        Nastavnik n = nastavnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));

        // Povuci sva zvanja direktno iz repozitorijuma po nastavniku
        List<NastavnikZvanje> zvanja = nastavnikZvanjeRepository.findByNastavnikId(n.getId());

        return zvanja.stream()
                .map(z -> new NastavnikZvanjeResponse(
                        z.getDatumIzbora(),
                        z.getNaucnaOblast(),
                        z.getUzaNaucnaOblast(),
                        z.getZvanje(),
                        z.isAktivno(),
                        NastavnikConverter.toNastavnikResponse(n)
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NastavnikZvanjeResponse getAktivnoZvanje(String email) {
        Nastavnik n = nastavnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));

        Optional<NastavnikZvanje> aktivno = nastavnikZvanjeRepository.findByNastavnikIdAndAktivno(n.getId(), true);

        return aktivno.map(z -> new NastavnikZvanjeResponse(
                        z.getDatumIzbora(),
                        z.getNaucnaOblast(),
                        z.getUzaNaucnaOblast(),
                        z.getZvanje(),
                        true,
                        NastavnikConverter.toNastavnikResponse(n)
                ))
                .orElseThrow(() -> new IllegalArgumentException("Nastavnik nema aktivno zvanje."));
    }


    @Transactional(readOnly = true)
    public List<VisokoSkolskaUstanovaResponse> getUstanove(String email) {
        Nastavnik n = nastavnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));

        List<VisokoSkolskaUstanovaResponse> responseList = new ArrayList<>();
        List<VisokoSkolskaUstanova> ustanove = (List<VisokoSkolskaUstanova>) ustanovaRepository.findAll(); // Ako želiš filter po nastavniku, može se dodati query
        for (VisokoSkolskaUstanova u : ustanove) {
            responseList.add(VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponse(u));
        }
        return responseList;
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> getDrziPredmete(String email) {
        Nastavnik n = nastavnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Nastavnik sa email-om " + email + " ne postoji!"));

        List<DrziPredmetResponse> responseList = new ArrayList<>();
        List<DrziPredmet> predmeti = (List<DrziPredmet>) drziPredmetRepository.findAll(); // Može se dodati query po nastavnik.email
        for (DrziPredmet dp : predmeti) {
            if (dp.getNastavnik() != null && dp.getNastavnik().getEmail().equals(email)) {
                responseList.add(DrziPredmetConverter.toDrziPredmetResponse(dp));
            }
        }
        return responseList;
    }
}
