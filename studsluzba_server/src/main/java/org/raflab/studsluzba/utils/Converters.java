package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.entity.*;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    //-------------------------NASTAVNIK-------------------------
    public static Nastavnik toNastavnik(NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(nastavnikRequest.getIme());
        nastavnik.setPrezime(nastavnikRequest.getPrezime());
        nastavnik.setSrednjeIme(nastavnikRequest.getSrednjeIme());
        nastavnik.setEmail(nastavnikRequest.getEmail());
        nastavnik.setBrojTelefona(nastavnikRequest.getBrojTelefona());
        nastavnik.setAdresa(nastavnikRequest.getAdresa());
        nastavnik.setZvanja(nastavnikRequest.getZvanja());
        nastavnik.setDatumRodjenja(nastavnikRequest.getDatumRodjenja());
        nastavnik.setPol(nastavnikRequest.getPol());
        nastavnik.setJmbg(nastavnikRequest.getJmbg());
        return nastavnik;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik)
    {
        NastavnikResponse response = new NastavnikResponse();
        response.setId(nastavnik.getId());
        response.setIme(nastavnik.getIme());
        response.setPrezime(nastavnik.getPrezime());
        response.setSrednjeIme(nastavnik.getSrednjeIme());
        response.setEmail(nastavnik.getEmail());
        response.setBrojTelefona(nastavnik.getBrojTelefona());
        response.setAdresa(nastavnik.getAdresa());
        response.setZvanja(nastavnik.getZvanja());

        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }
    public static List<NastavnikResponse> toNastavnikResponseList(Iterable<Nastavnik> nastavnikIterable) {
        List<NastavnikResponse> nastavnikResponses = new ArrayList<>();

        nastavnikIterable.forEach((nastavnik) -> {
            nastavnikResponses.add(toNastavnikResponse(nastavnik));
        });
        return nastavnikResponses;
    }
    //-------------------------STUDENT I STUDENT INDEX-------------------------
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

    //-------------------------STUDIJSKI PROGRAM-------------------------
    public static StudijskiProgram toStudijskiProgram(StudijskiProgramRequest request) {
        StudijskiProgram studijskiProgram = new StudijskiProgram();
        studijskiProgram.setNaziv(request.getNaziv());
        studijskiProgram.setOznaka(request.getOznaka());
        studijskiProgram.setZvanje(request.getZvanje());
        studijskiProgram.setGodinaAkreditacije(request.getGodinaAkreditacije());
        studijskiProgram.setTrajanjeGodina(request.getTrajanjeGodina());
        studijskiProgram.setUkupnoEspb(request.getUkupnoEspb());
        studijskiProgram.setVrstaStudija(request.getVrstaStudija());
        studijskiProgram.setTrajanjeSemestara(request.getTrajanjeSemestara());
        return studijskiProgram;
    }

    public static StudijskiProgramRequest toStudijskiProgramRequest(StudijskiProgram studijskiProgram) {
        StudijskiProgramRequest request = new StudijskiProgramRequest();
        request.setNaziv(studijskiProgram.getNaziv());
        request.setOznaka(studijskiProgram.getOznaka());
        request.setZvanje(studijskiProgram.getZvanje());
        request.setGodinaAkreditacije(studijskiProgram.getGodinaAkreditacije());
        request.setTrajanjeGodina(studijskiProgram.getTrajanjeGodina());
        request.setUkupnoEspb(studijskiProgram.getUkupnoEspb());
        request.setVrstaStudija(studijskiProgram.getVrstaStudija());
        request.setTrajanjeSemestara(studijskiProgram.getTrajanjeSemestara());
        return request;
    }

    public static StudijskiProgram toStudijskiProgram(StudijskiProgramNewRequest studijskiProgramNewRequest) {
        StudijskiProgram studijskiProgram = new StudijskiProgram();
        studijskiProgram.setNaziv(studijskiProgramNewRequest.getNaziv());
        studijskiProgram.setOznaka(studijskiProgramNewRequest.getOznaka());
        studijskiProgram.setZvanje(studijskiProgramNewRequest.getZvanje());
        studijskiProgram.setGodinaAkreditacije(studijskiProgramNewRequest.getGodinaAkreditacije());
        studijskiProgram.setTrajanjeGodina(studijskiProgramNewRequest.getTrajanjeGodina());
        studijskiProgram.setUkupnoEspb(studijskiProgramNewRequest.getUkupnoEspb());
        studijskiProgram.setVrstaStudija(studijskiProgramNewRequest.getVrstaStudija());
        studijskiProgram.setTrajanjeSemestara(studijskiProgramNewRequest.getTrajanjeSemestara());
        return studijskiProgram;
    }

    public static StudijskiProgramResponse toStudijskiProgramResponse(StudijskiProgram studijskiProgram){
        StudijskiProgramResponse studijskiProgramResponse=new StudijskiProgramResponse(studijskiProgram.getOznaka(), studijskiProgram.getNaziv(), studijskiProgram.getGodinaAkreditacije(), studijskiProgram.getZvanje(), studijskiProgram.getTrajanjeGodina(), studijskiProgram.getTrajanjeSemestara(), studijskiProgram.getVrstaStudija(), studijskiProgram.getUkupnoEspb());
        return studijskiProgramResponse;
    }

    public static List<StudijskiProgramResponse> toStudijskiProgramResponseList(List<StudijskiProgram> studijskiProgramList) {
        List<StudijskiProgramResponse> studijskiProgramResponses = new ArrayList<>();

        studijskiProgramList.forEach((studijskiProgram) -> {
            studijskiProgramResponses.add(toStudijskiProgramResponse(studijskiProgram));
        });
        return studijskiProgramResponses;
    }
    //-------------------------VISOKOSKOLSKEUSTANOVE-------------------------
    public static VisokoSkolskaUstanova toVisokoSkolskaUstanova(VisokoSkolskaUstanovaRequest  visokoSkolskaUstanovaRequest) {
        VisokoSkolskaUstanova visokoSkolskaUstanova=new VisokoSkolskaUstanova();
        visokoSkolskaUstanova.setNaziv(visokoSkolskaUstanovaRequest.getNaziv());
        visokoSkolskaUstanova.setVrsta(visokoSkolskaUstanovaRequest.getVrsta());

        return visokoSkolskaUstanova;
    }

    public static VisokoSkolskaUstanovaResponse toVisokoSkolskaUstanovaResponse(VisokoSkolskaUstanova visokoSkolskaUstanova) {
        return new VisokoSkolskaUstanovaResponse(visokoSkolskaUstanova.getNaziv(), visokoSkolskaUstanova.getVrsta().name());
    }

    public static List<VisokoSkolskaUstanovaResponse> toVisokoSkolskaUstanovaResponseList(List<VisokoSkolskaUstanova> vsuList) {
        List<VisokoSkolskaUstanovaResponse> responseList = new ArrayList<>();
        for (VisokoSkolskaUstanova vsu : vsuList) {
            responseList.add(toVisokoSkolskaUstanovaResponse(vsu));
        }
        return responseList;
    }
    //-------------------------PREDMET-------------------------
    public static Predmet toPredmet(PredmetRequest request, StudijskiProgram studProgram) {
        Predmet predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setOpis(request.getOpis());
        predmet.setEspb(request.getEspb());
        predmet.setObavezan(request.isObavezan());
        // Postavi postojeći entitet StudijskiProgram
        predmet.setStudProgram(studProgram);
        return predmet;
    }


    public static PredmetResponse toPredmetResponse(Predmet predmet) {
        return new PredmetResponse(
                predmet.getSifra(),
                predmet.getNaziv(),
                predmet.getOpis(),
                predmet.getEspb(),
                predmet.getStudProgram().getOznaka(), // SAMO oznaka kao String
                predmet.isObavezan()
        );
    }



    public static List<PredmetResponse> toPredmetResponseList(List<Predmet> predmetList) {
        List<PredmetResponse> responseList = new ArrayList<>();
        for (Predmet predmet : predmetList) {
            responseList.add(toPredmetResponse(predmet));
        }
        return responseList;
    }

    //-------------------------SREDNJA SKOLA-------------------------
    public static SrednjaSkola toSrednjaSkola(SrednjaSkolaRequest request) {
        SrednjaSkola srednjaSkola = new SrednjaSkola();
        srednjaSkola.setNaziv(request.getNaziv());
        srednjaSkola.setVrstaSrednjeSkole(request.getVrstaSrednjeSkole());
        return srednjaSkola;
    }

    public static SrednjaSkolaResponse toSrednjaSkolaResponse(SrednjaSkola srednjaSkola) {
        return new SrednjaSkolaResponse(
                srednjaSkola.getNaziv(),
                srednjaSkola.getVrstaSrednjeSkole()
        );
    }

    public static List<SrednjaSkolaResponse> toSrednjaSkolaResponseList(Iterable<SrednjaSkola> skole) {
        List<SrednjaSkolaResponse> responseList = new ArrayList<>();
        skole.forEach(s -> responseList.add(toSrednjaSkolaResponse(s)));
        return responseList;
    }


}
