package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.response.IzlazakNaIspitResponse;
import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.raflab.studsluzba.model.entity.PrijavaIspita;

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
        double ukupnoPoena = izlazakNaIspit.getBrojPoenaSaIspita() + izlazakNaIspit.getBrojPredispitnihPoena();

        return new IzlazakNaIspitResponse(
                izlazakNaIspit.getId(),
                izlazakNaIspit.getBrojPoenaSaIspita(),
                izlazakNaIspit.getBrojPredispitnihPoena(),
                izlazakNaIspit.getNapomena(),
                izlazakNaIspit.isPonistava(),
                izlazakNaIspit.getPrijavaIspita() != null ? izlazakNaIspit.getPrijavaIspita().getId() : null
        );
    }
}
