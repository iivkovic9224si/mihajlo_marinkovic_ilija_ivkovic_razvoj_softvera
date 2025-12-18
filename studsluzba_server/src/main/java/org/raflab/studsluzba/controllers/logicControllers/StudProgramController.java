package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.StudProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import request.StudijskiProgramNewRequest;
import request.StudijskiProgramRequest;
import response.PredmetResponse;
import response.StudijskiProgramResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/studprogram")
public class StudProgramController {

    @Autowired
    StudProgramService studProgramService;

    // Vraca predmete za studijski program
    @PostMapping(path = "/predmeti")
    public List<PredmetResponse> predmeti(@RequestBody @Valid StudijskiProgramRequest studijskiProgramRequest) {
        return studProgramService.findPredmetByStudProgram(studijskiProgramRequest);
    }

    // Dodaje novi studijski program
    @PostMapping(path= "/add")
    public Long add(@RequestBody @Valid StudijskiProgramNewRequest studijskiProgramNewRequest) {
        return studProgramService.save(studijskiProgramNewRequest);
    }

    // Update postojeceg studijskog programa po ID
    @PostMapping(path= "/update/{id}")
    public StudijskiProgramResponse update(
            @PathVariable Long id,
            @RequestBody @Valid StudijskiProgramNewRequest studijskiProgramNewRequest) {
        return studProgramService.update(id, studijskiProgramNewRequest);
    }

    // Ispisuje sve studijske programe
    @GetMapping(path= "/all")
    public List<StudijskiProgramResponse> all() {
        return studProgramService.findAll();
    }

    // Ispisuje studijski program po ID
    @GetMapping(path="/show/{id}")
    public StudijskiProgramResponse findById(@PathVariable Long id) {
        Optional<StudijskiProgramResponse> studijskiProgramResponse = studProgramService.findById(id);
        return studijskiProgramResponse.orElse(null);
    }

    // Brise studijski program po ID
    @GetMapping(path="/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        studProgramService.deleteStudijskiProgram(id);
    }

    // Vraca sve oznake studijskih programa
    @GetMapping(path="/oznake")
    public List<String> findAllOznake() {
        return studProgramService.findAllOznake();
    }

    // Vraca sve studijske programe sortirane po godini akreditacije
    @GetMapping(path="/all/sorted")
    public List<StudijskiProgramResponse> getAllSortedByGodinaDesc() {
        return studProgramService.getAllSortedByGodinaDesc();
    }
}
