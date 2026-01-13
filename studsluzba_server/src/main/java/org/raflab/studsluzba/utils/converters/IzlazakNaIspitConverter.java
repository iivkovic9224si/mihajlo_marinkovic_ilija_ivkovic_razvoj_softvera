package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.raflab.studsluzba.model.entity.PrijavaIspita;
import request.IzlazakNaIspitRequest;
import response.IzlazakNaIspitResponse;

public class IzlazakNaIspitConverter {

    public static IzlazakNaIspit toEntity(IzlazakNaIspitRequest izlazakNaIspitRequest, PrijavaIspita prijavaIspita) {
        IzlazakNaIspit izlazakNaIspit = new IzlazakNaIspit();
        izlazakNaIspit.setId(izlazakNaIspitRequest.getId());
        izlazakNaIspit.setBrojPoenaSaIspita(izlazakNaIspitRequest.getBrojPoenaSaIspita());
        izlazakNaIspit.setBrojPredispitnihPoena(izlazakNaIspitRequest.getBrojPredispitnihPoena());
        izlazakNaIspit.setNapomena(izlazakNaIspitRequest.getNapomena());
        izlazakNaIspit.setPonistava(izlazakNaIspitRequest.isPonistava());
        izlazakNaIspit.setPrijavaIspita(prijavaIspita);
        return izlazakNaIspit;
    }

    public static IzlazakNaIspitResponse toResponse(IzlazakNaIspit izlazakNaIspit) {
        IzlazakNaIspitResponse r = new IzlazakNaIspitResponse();
        r.setId(izlazakNaIspit.getId());
        r.setBrojPoenaSaIspita(izlazakNaIspit.getBrojPoenaSaIspita());
        r.setBrojPredispitnihPoena(izlazakNaIspit.getBrojPredispitnihPoena());
        r.setNapomena(izlazakNaIspit.getNapomena());
        r.setPonistava(izlazakNaIspit.isPonistava());

        if (izlazakNaIspit.getPrijavaIspita() != null) {
            r.setPrijavaIspitaID(izlazakNaIspit.getPrijavaIspita().getId());
            r.setPrijavaIspita(PrijavaIspitaConverter.toPrijavaIspitaResponse(izlazakNaIspit.getPrijavaIspita()));
        }
        return r;
    }
}
