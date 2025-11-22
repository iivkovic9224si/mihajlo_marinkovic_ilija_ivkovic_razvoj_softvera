package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;
import org.raflab.studsluzba.model.entity.VrstaStudijskeAktivnosti;

public class StudijskiTokAktivnostiConverter {
    public static StudijskiTokAktivnosti toStrudijskiTokAktivnosti(StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest, StudentIndeks studentIndeks) {
        StudijskiTokAktivnosti studijskiTokAktivnosti = new StudijskiTokAktivnosti();
        studijskiTokAktivnosti.setTip(VrstaStudijskeAktivnosti.valueOf(studijskiTokAktivnostiRequest.getTip()));
        studijskiTokAktivnosti.setDatum(studijskiTokAktivnostiRequest.getDatum());
        studijskiTokAktivnosti.setGodina(studijskiTokAktivnostiRequest.getGodina());
        studijskiTokAktivnosti.setNapomena(studijskiTokAktivnostiRequest.getNapomena());
        studijskiTokAktivnosti.setStudentIndeks(studentIndeks);
        return studijskiTokAktivnosti;
    }
}
