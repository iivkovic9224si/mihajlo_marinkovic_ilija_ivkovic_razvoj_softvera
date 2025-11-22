package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.OstvareniPredispitniRequest;
import org.raflab.studsluzba.controllers.response.OstvareniPredispitniResponse;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.OstvareniPredispitni;
import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.services.PredispitneObavezeService;
import org.raflab.studsluzba.services.SlusaPredmetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OstvareniPredispitniConverter {

    public static OstvareniPredispitni toEntity(OstvareniPredispitniRequest request,
                                                PredispitneObavezeService predispitneObavezeService,
                                                SlusaPredmetService slusaPredmetService) {

        OstvareniPredispitni entity = new OstvareniPredispitni();
        entity.setId(request.getId());
        entity.setOstvareniPoeni(request.getOstvareniPoeni());

        PredispitneObavezeResponse poResp =
                predispitneObavezeService.findById(request.getPredispitneObavezeRequest().getId());
        SlusaPredmetResponse spResp =
                slusaPredmetService.findById(request.getSlusaPredmetRequest().getId()).get();

        PredispitneObaveze po = new PredispitneObaveze();
        po.setId(poResp.getId());
        po.setMaksimalanBrojPoena(poResp.getMaksimalanBrojPoena());
        po.setVrstaPredispitneObaveze(poResp.getVrstaPredispitneObaveze());
        entity.setPredispitneObaveze(po);

        SlusaPredmet sp = new SlusaPredmet();
        sp.setId(spResp.getId());
        entity.setSlusaPredmet(sp);

        return entity;
    }

    public static OstvareniPredispitniResponse toResponse(OstvareniPredispitni entity) {
        OstvareniPredispitniResponse response = new OstvareniPredispitniResponse(
                entity.getId(),
                entity.getOstvareniPoeni(),
                PredispitneObavezeConverter.toResponse(entity.getPredispitneObaveze()),
                SlusaPredmetConverter.toSlusaPredmetResponse(entity.getSlusaPredmet())
        );
        return response;
    }

    public static List<OstvareniPredispitniResponse> toResponseList(List<OstvareniPredispitni> lista) {
        List<OstvareniPredispitniResponse> responses = new ArrayList<>();
        for (OstvareniPredispitni e : lista) {
            responses.add(toResponse(e));
        }
        return responses;
    }
}
