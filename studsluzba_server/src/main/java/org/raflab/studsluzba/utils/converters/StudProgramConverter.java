package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.StudijskiProgram;
import request.StudijskiProgramNewRequest;
import request.StudijskiProgramRequest;
import response.StudijskiProgramResponse;

import java.util.ArrayList;
import java.util.List;

public class StudProgramConverter {
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

}
