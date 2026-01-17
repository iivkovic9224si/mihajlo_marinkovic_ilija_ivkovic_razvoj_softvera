package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.PredmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public PredmetResponse save(@RequestBody @Valid PredmetRequest request) {
        return predmetService.save(request);
    }

    // 👉 PRETVARANJE IllegalStateException u 409
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(IllegalStateException ex) {
        return ex.getMessage();
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
            @RequestParam String studProgramOznaka,
            @RequestParam(required = false) Integer indeksGodina,
            @RequestParam(required = false) Integer indeksBroj
    ) {
        if (indeksGodina != null && indeksBroj != null) {
            // ✅ Filter položenih
            return predmetService.getDostupniZaUpis(maxGodina, studProgramOznaka, indeksGodina, indeksBroj);
        }
        // ✅ Bez filtera
        return predmetService.getDostupniZaUpis(indeksGodinaBroj, maxGodina, studProgramOznaka);
    }

}