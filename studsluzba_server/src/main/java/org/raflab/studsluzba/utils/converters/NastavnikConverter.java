package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;

import java.util.ArrayList;
import java.util.List;

public class NastavnikConverter {

    public static Nastavnik toNastavnik(NastavnikRequest request) {
        Nastavnik n = new Nastavnik();
        n.setId(request.getId());
        n.setIme(request.getIme());
        n.setPrezime(request.getPrezime());
        n.setSrednjeIme(request.getSrednjeIme());
        n.setEmail(request.getEmail());
        n.setBrojTelefona(request.getBrojTelefona());
        n.setAdresa(request.getAdresa());
        n.setDatumRodjenja(request.getDatumRodjenja());
        n.setPol(request.getPol());
        n.setJmbg(request.getJmbg());
        return n;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik) {
        NastavnikResponse res = new NastavnikResponse();
        res.setId(nastavnik.getId());
        res.setIme(nastavnik.getIme());
        res.setPrezime(nastavnik.getPrezime());
        res.setSrednjeIme(nastavnik.getSrednjeIme());
        res.setEmail(nastavnik.getEmail());
        res.setBrojTelefona(nastavnik.getBrojTelefona());
        res.setAdresa(nastavnik.getAdresa());
        res.setDatumRodjenja(nastavnik.getDatumRodjenja());
        res.setPol(nastavnik.getPol());
        res.setJmbg(nastavnik.getJmbg());
        // liste (zvanja, držiPredmete, ustanove) ne učitavamo ovde
        return res;
    }

    public static List<NastavnikResponse> toNastavnikResponseList(List<Nastavnik> lista) {
        List<NastavnikResponse> responses = new ArrayList<>();
        for (Nastavnik n : lista) {
            responses.add(toNastavnikResponse(n));
        }
        return responses;
    }
}
