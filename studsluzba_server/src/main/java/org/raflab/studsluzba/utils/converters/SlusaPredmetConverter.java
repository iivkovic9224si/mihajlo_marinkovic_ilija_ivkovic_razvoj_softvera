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
        SlusaPredmetResponse slusaPredmetResponse = new SlusaPredmetResponse(
                slusaPredmet.getSkolskaGodina().getGodina(), // umesto toString()
                slusaPredmet.getStudentIndeks().getStudProgramOznaka() + slusaPredmet.getStudentIndeks().getBroj() + slusaPredmet.getStudentIndeks().getGodina(), // primer, zavisi šta ti treba
                slusaPredmet.getDrziPredmet().getNastavnik().getIme(), // samo konkretna polja
                slusaPredmet.getDrziPredmet().getPredmet().getSifra() // samo konkretna polja
        );
        return slusaPredmetResponse;
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
