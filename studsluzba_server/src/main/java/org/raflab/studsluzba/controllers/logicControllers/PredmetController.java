package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.PredmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import request.PredmetRequest;
import response.PredmetResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/predmet")
public class PredmetController {

    @Autowired
    private PredmetService predmetService;

    @PostMapping("/save")
    public void save(@RequestBody @Valid PredmetRequest request) {
        predmetService.save(request);
    }

    @GetMapping(path="/findAll")
    public List<PredmetResponse> findAll() {
        return predmetService.findAll();
    }

    @GetMapping(path="/findById/{id}")
    public PredmetResponse findById(@PathVariable Long id) {
        return predmetService.findById(id);
    }

    @PostMapping(path= "/update/{sifra}")
    public PredmetResponse findBySifra(@PathVariable String sifra,@RequestBody @Valid PredmetRequest predmetRequest) {
        return predmetService.update(sifra,predmetRequest);
    }

    @GetMapping(path = "/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        predmetService.delete(id);
    }

    @GetMapping(path="/godinaAkreditacije/{godinaAkreditacije}")
    public List<PredmetResponse> findByGodinaAkreditacije (@PathVariable Integer godinaAkreditacije) {
        return predmetService.findByGodinaAkreditacije(godinaAkreditacije);
    }

    @PostMapping(path="/obavezniPredmeti")
    public List<PredmetResponse> findByStudProgramAndObavezan(@RequestParam String oznakaStudProgram,
                                                              @RequestParam boolean obavezan) {
        return predmetService.findByStudProgramAndObavezan(oznakaStudProgram, obavezan);
    }
    @GetMapping("/prosecnaOcena/{sifraPredmeta}")
    public Double getProsecnaOcena(
            @PathVariable String sifraPredmeta,
            @RequestParam int godinaOd,
            @RequestParam int godinaDo
    ) {
        return predmetService.getProsecnaOcena(sifraPredmeta, godinaOd, godinaDo);
    }

    @GetMapping(path = "/dostupni")
    public List<PredmetResponse> getDostupniPredmeti(
            @RequestParam String indeksGodinaBroj,
            @RequestParam int maxGodina,
            @RequestParam String studProgramOznaka
    ) {
        return predmetService.getDostupniZaUpis(indeksGodinaBroj, maxGodina, studProgramOznaka);
    }

}