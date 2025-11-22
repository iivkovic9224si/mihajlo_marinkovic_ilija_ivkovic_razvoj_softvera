package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SlusaPredmetConverter {
    public static SlusaPredmet toSlusaPredmet(SkolskaGodina skolskaGodina, StudentIndeks studentIndeks, DrziPredmet drziPredmet) {
        SlusaPredmet slusaPredmet = new SlusaPredmet();
        slusaPredmet.setDrziPredmet(drziPredmet);
        slusaPredmet.setStudentIndeks(studentIndeks);
        slusaPredmet.setSkolskaGodina(skolskaGodina);
        return slusaPredmet;
    }

    public static SlusaPredmetResponse toSlusaPredmetResponse(SlusaPredmet slusaPredmet) {
        String godina = slusaPredmet.getSkolskaGodina() != null ? slusaPredmet.getSkolskaGodina().getGodina() : "Nepoznata godina";
        String student = slusaPredmet.getStudentIndeks() != null ?
                slusaPredmet.getStudentIndeks().getStudProgramOznaka() +
                        slusaPredmet.getStudentIndeks().getBroj() +
                        slusaPredmet.getStudentIndeks().getGodina() : "Nepoznat student";
        String nastavnik = slusaPredmet.getDrziPredmet() != null && slusaPredmet.getDrziPredmet().getNastavnik() != null ?
                slusaPredmet.getDrziPredmet().getNastavnik().getIme() : "Nepoznati nastavnik";
        String sifraPredmeta = slusaPredmet.getDrziPredmet() != null && slusaPredmet.getDrziPredmet().getPredmet() != null ?
                slusaPredmet.getDrziPredmet().getPredmet().getSifra() : "Nepoznata sifra";

        return new SlusaPredmetResponse(godina, student, nastavnik, sifraPredmeta);
    }


    public static List<SlusaPredmetResponse> toSlusaPredmetResponseList(List<SlusaPredmet> slusaPredmeti) {
        return slusaPredmeti.stream()
                .map(SlusaPredmetConverter::toSlusaPredmetResponse)
                .collect(Collectors.toList());
    }

    public static List<SlusaPredmetResponse> toSlusaPredmetResponseList(Iterable<SlusaPredmet> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(SlusaPredmetConverter::toSlusaPredmetResponse)
                .collect(Collectors.toList());
    }

}
