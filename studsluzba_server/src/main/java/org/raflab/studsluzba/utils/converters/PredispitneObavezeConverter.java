package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.services.PredmetService;
import request.PredispitneObavezeRequest;
import response.PredispitneObavezeResponse;
import response.PredmetResponse;

import java.util.ArrayList;
import java.util.List;

public class PredispitneObavezeConverter {

    public static PredispitneObaveze toEntity(PredispitneObavezeRequest request, PredmetService predmetService) {
        PredispitneObaveze entity = new PredispitneObaveze();
        entity.setId(request.getId());
        entity.setMaksimalanBrojPoena(request.getMaksimalanBrojPoena());
        entity.setVrstaPredispitneObaveze(request.getVrstaPredispitneObaveze());

        // Dohvatanje Predmeta preko sifre iz requesta
        PredmetResponse predmetResponse = predmetService.findBySifra(request.getPredmetRequest().getSifra());
        Predmet predmetEntity = new Predmet();
        predmetEntity.setSifra(predmetResponse.getSifra());
        predmetEntity.setNaziv(predmetResponse.getNaziv());
        predmetEntity.setOpis(predmetResponse.getOpis());
        predmetEntity.setEspb(predmetResponse.getEspb());
        predmetEntity.setObavezan(predmetResponse.isObavezan());

        entity.setPredmet(predmetEntity);
        return entity;
    }

    public static PredispitneObavezeResponse toResponse(PredispitneObaveze entity) {
        PredispitneObavezeResponse response = new PredispitneObavezeResponse();
        response.setId(entity.getId());
        response.setMaksimalanBrojPoena(entity.getMaksimalanBrojPoena());
        response.setVrstaPredispitneObaveze(entity.getVrstaPredispitneObaveze());

        Predmet predmet = entity.getPredmet();
        if (predmet != null) {
            response.setPredmetResponse(
                    new PredmetResponse(
                            predmet.getSifra(),
                            predmet.getNaziv(),
                            predmet.getOpis(),
                            predmet.getEspb(),
                            predmet.getStudProgram() != null ? predmet.getStudProgram().getOznaka() : null,
                            predmet.isObavezan(),
                            predmet.getGodinaStudija()
                    )
            );
        }
        return response;
    }

    public static List<PredispitneObavezeResponse> toResponseList(List<PredispitneObaveze> list) {
        List<PredispitneObavezeResponse> responses = new ArrayList<>();
        for (PredispitneObaveze entity : list) {
            responses.add(toResponse(entity));
        }
        return responses;
    }
}
