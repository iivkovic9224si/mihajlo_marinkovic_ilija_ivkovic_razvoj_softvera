package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;
import org.raflab.studsluzba.model.entity.VrstaStudijskeAktivnosti;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;

import java.util.ArrayList;
import java.util.List;

import static org.raflab.studsluzba.utils.converters.StudProgramConverter.toStudijskiProgramResponse;

public class StudentIndexAktivnostController {
    public static StudentPodaci toStudentPodaci(StudentPodaciRequest request) {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setIme(request.getIme());
        studentPodaci.setPrezime(request.getPrezime());
        studentPodaci.setSrednjeIme(request.getSrednjeIme());
        studentPodaci.setJmbg(request.getJmbg());
        studentPodaci.setDatumRodjenja(request.getDatumRodjenja());
        studentPodaci.setMestoRodjenja(request.getMestoRodjenja());
        studentPodaci.setMestoPrebivalista(request.getMestoPrebivalista());
        studentPodaci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        studentPodaci.setDrzavljanstvo(request.getDrzavljanstvo());
        studentPodaci.setNacionalnost(request.getNacionalnost());
        studentPodaci.setPol(request.getPol());
        studentPodaci.setAdresa(request.getAdresa());
        studentPodaci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        studentPodaci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        studentPodaci.setEmail(request.getEmail());
        studentPodaci.setBrojLicneKarte(request.getBrojLicneKarte());
        studentPodaci.setLicnuKartuIzdao(request.getLicnuKartuIzdao());
        studentPodaci.setMestoStanovanja(request.getMestoStanovanja());
        studentPodaci.setAdresaStanovanja(request.getAdresaStanovanja());
        return studentPodaci;
    }

    public static StudentIndeks toStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setGodina(studentIndeksRequest.getGodina());
        studentIndeks.setStudProgramOznaka(studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setNacinFinansiranja(studentIndeksRequest.getNacinFinansiranja());
        studentIndeks.setAktivan(studentIndeksRequest.isAktivan());
        studentIndeks.setVaziOd(studentIndeksRequest.getVaziOd());
        return studentIndeks;
    }

    public static StudentIndeksResponse toStudentIndeksResponse(StudentIndeks si) {
        if (si == null) return null;

        return new StudentIndeksResponse(
                si.getId(),
                si.getBroj(),
                si.getGodina(),
                si.getStudProgramOznaka(),
                si.getNacinFinansiranja(),
                si.isAktivan(),
                si.getVaziOd(),
                si.getStudent(),
                toStudijskiProgramResponse(si.getStudijskiProgram()),
                si.getOstvarenoEspb()
        );
    }
    public static StudijskiTokAktivnosti toStudijskiTokAktivnosti(StudijskiTokAktivnostiRequest req, StudentIndeksRepository studentIndeksRepository) {
        if(req == null) return null;

        VrstaStudijskeAktivnosti tip;
        try {
            tip = VrstaStudijskeAktivnosti.valueOf(req.getTip());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Nepoznat tip studijskog toka: " + req.getTip());
        }

        StudentIndeks studentIndeks = studentIndeksRepository
                .findByGodinaAndBrojAndStudProgramOznakaAndAktivan(
                        req.getIndeksGodina(),
                        req.getIndeksBroj(),
                        req.getIndeksStudProgramOznaka(),
                        req.isIndeksAktivan()
                ).orElseThrow(() -> new RuntimeException(
                        "StudentIndeks ne postoji: " + req.getIndeksBroj() + "/" + req.getIndeksGodina()
                ));

        return new StudijskiTokAktivnosti(
                tip,
                req.getGodina(),
                req.getDatum(),
                req.getNapomena(),
                studentIndeks
        );
    }


    public static StudijskiTokAktivnostiResponse toStudijskiTokAktivnostiResponse(StudijskiTokAktivnosti si) {
        if(si == null) return null;
        return new StudijskiTokAktivnostiResponse(
                si.getTip().name(),
                si.getGodina(),
                si.getDatum(),
                si.getNapomena(),
                toStudentIndeksResponse(si.getStudentIndeks())
        );
    }

    public static List<StudijskiTokAktivnosti> toStudijskiTokAktivnostiList(
            List<StudijskiTokAktivnostiRequest> siList,
            StudentIndeksRepository studentIndeksRepository) {

        List<StudijskiTokAktivnosti> resList = new ArrayList<>();
        if (siList != null) {
            siList.forEach(si -> {
                resList.add(toStudijskiTokAktivnosti(si, studentIndeksRepository));
            });
        }
        return resList;
    }


    public static List<StudijskiTokAktivnostiResponse> toStudijskiTokAktivnostiResponseList(List<StudijskiTokAktivnosti> siList) {
        List<StudijskiTokAktivnostiResponse> resList = new ArrayList<>();
        if (siList != null) {
            siList.forEach(si -> resList.add(toStudijskiTokAktivnostiResponse(si)));
        }
        return resList;
    }
}
