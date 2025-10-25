package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.StudijskiProgramNewRequest;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.services.StudProgramService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    //dodaje studProgram
    @PostMapping(path= "/add")
    public Long add(@RequestBody @Valid StudijskiProgramNewRequest studijskiProgramNewRequest) {
        StudijskiProgram studijskiProgram=studProgramService.save(Converters.toStudijskiProgram(studijskiProgramNewRequest));
        return studijskiProgram.getId();
    }

    //update postojeceg ID-ja
    @PostMapping(path= "/update/{id}")
    public StudijskiProgramResponse update(
            @PathVariable Long id,
            @RequestBody @Valid StudijskiProgramNewRequest studijskiProgramNewRequest) {
        StudijskiProgram updatedProgram = Converters.toStudijskiProgram(studijskiProgramNewRequest);
        updatedProgram.setId(id);

        StudijskiProgram savedProgram = studProgramService.update(updatedProgram);
        return Converters.toStudijskiProgramResponse(savedProgram);
    }

    //ispisuje sve studProgram
    @GetMapping(path= "/all")
    public List<StudijskiProgramResponse> all() {
        return studProgramService.findAll();
    }

    //ispisuje studProgram po ID
    @GetMapping(path="/show/{id}")
    public StudijskiProgramResponse findById(@PathVariable Long id) {
        Optional<StudijskiProgramResponse> studijskiProgramResponse=studProgramService.findById(id);
        return studijskiProgramResponse.orElse(null);
    }
    //brise studProgram po ID
    @GetMapping(path="/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        studProgramService.deleteStudijskiProgram(id);
    }

    @GetMapping(path="/oznake")
    public List<String> findAllOznake() {
        return new ArrayList<>(studProgramService.findAllOznake());
    }
    //
    @GetMapping(path="/all/sorted")
    public List<StudijskiProgramResponse> getAllSortedByGodinaDesc() {
        return new ArrayList<>(studProgramService.getAllSortedByGodinaDesc());
    }


}
