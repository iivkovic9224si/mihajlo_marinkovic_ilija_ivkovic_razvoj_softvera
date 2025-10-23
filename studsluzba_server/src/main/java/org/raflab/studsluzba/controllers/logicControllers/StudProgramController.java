package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.services.StudProgramService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/studprogram")

public class StudProgramController {

    @Autowired
    StudProgramService studProgramService;

    @PostMapping(path = "/predmeti")
    public List<PredmetResponse> predmeti(@RequestBody @Valid StudijskiProgramRequest studijskiProgramRequest) {
        List<PredmetResponse> predmeti = studProgramService.findPredmetByStudProgram(Converters.toStudijskiProgram(studijskiProgramRequest));
        return predmeti;
    }

}
