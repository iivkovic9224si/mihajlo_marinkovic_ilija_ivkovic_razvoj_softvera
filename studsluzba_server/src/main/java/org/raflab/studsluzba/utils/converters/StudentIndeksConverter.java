package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.StudentIndeks;
import request.StudentIndeksRequest;

public class StudentIndeksConverter {
    public static StudentIndeks toStudentIndeks(StudentIndeksRequest req) {
        if (req == null) return null;

        StudentIndeks indeks = new StudentIndeks();

        indeks.setGodina(req.getGodina());
        indeks.setStudProgramOznaka(req.getStudProgramOznaka());
        indeks.setBroj(req.getBroj());
        indeks.setNacinFinansiranja(req.getNacinFinansiranja());
        indeks.setAktivan(req.isAktivan());

        return indeks;
    }

}
