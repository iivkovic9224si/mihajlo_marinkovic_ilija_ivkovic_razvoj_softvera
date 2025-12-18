package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.Uplata;
import request.UplataRequest;
import response.UplataResponse;

public class UplataConverter {
    public static Uplata toUplata(UplataRequest request, StudentPodaci studentPodaci, SkolskaGodina skolskaGodina, Double srednjiKurs) {
        Uplata uplata = new Uplata();
        uplata.setDatumUplate(request.getDatumUplate());
        uplata.setIznosUDinarima(request.getIznosUDinarima());
        uplata.setStudentPodaci(studentPodaci);
        uplata.setSkolskaGodina(skolskaGodina);
        uplata.setSrednjiKurs(srednjiKurs);
        return uplata;
    }

    public static UplataResponse toUplataResponse(Uplata uplata, Double iznosUEurima) {
        UplataResponse response = new UplataResponse();
        if (uplata.getStudentPodaci() != null) {
            response.setStudent_jmbg(uplata.getStudentPodaci().getJmbg());
        }
        response.setIznosUDinarima(uplata.getIznosUDinarima());
        response.setIznosUEurima(iznosUEurima);
        return response;
    }
}
