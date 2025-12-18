package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.web.bind.annotation.*;
import request.DrziPredmetRequest;
import response.DrziPredmetResponse;
import response.PredmetResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/drziPredmet")
@RequiredArgsConstructor
public class DrziPredmetController {

    private final DrziPredmetService drziPredmetService;

    //----------------------CREATE----------------------
    @PostMapping("/save")
    public DrziPredmetResponse save(@RequestBody @Valid DrziPredmetRequest request) {
        return drziPredmetService.save(request);
    }

    //----------------------READ----------------------
    @GetMapping("/findAll")
    public List<DrziPredmetResponse> findAll() {
        return drziPredmetService.findAll();
    }

    @GetMapping("/findById/{id}")
    public DrziPredmetResponse findById(@PathVariable Long id) {
        return drziPredmetService.findById(id);
    }

    @GetMapping("/findByNastavnikEmail/{email}")
    public List<DrziPredmetResponse> findByNastavnikEmail(@PathVariable String email) {
        return drziPredmetService.findByNastavnikEmail(email);
    }


    @GetMapping("/predmetiZaNastavnikaUAktivnoj/{nastavnikEmail}")
    public List<PredmetResponse> getPredmetiZaNastavnikaUAktivnojGodini(@PathVariable String nastavnikEmail) {
        return drziPredmetService.getPredmetiZaNastavnikaUAktivnojGodini(nastavnikEmail);
    }

    //----------------------UPDATE----------------------
    @PostMapping("/update/{id}")
    public DrziPredmetResponse update(@PathVariable Long id, @RequestBody @Valid DrziPredmetRequest request) {
        return drziPredmetService.update(id, request);
    }

    //----------------------DELETE----------------------
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        drziPredmetService.deleteById(id);
    }
}
