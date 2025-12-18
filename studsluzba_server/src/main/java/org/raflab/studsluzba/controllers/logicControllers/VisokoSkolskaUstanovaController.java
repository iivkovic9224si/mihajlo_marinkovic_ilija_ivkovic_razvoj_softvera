package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.VisokoSkolskaUstanovaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import request.VisokoSkolskaUstanovaRequest;
import response.VisokoSkolskaUstanovaResponse;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/visokoSkolskaUstanova")
public class VisokoSkolskaUstanovaController {

    @Autowired
    private VisokoSkolskaUstanovaService service;

    //----------------- CREATE -----------------
    @PostMapping("/save")
    public VisokoSkolskaUstanovaResponse save(@RequestBody VisokoSkolskaUstanovaRequest request) {
        return service.save(request);
    }

    //----------------- READ ALL -----------------
    @GetMapping("/all")
    public List<VisokoSkolskaUstanovaResponse> findAll() {
        return service.findAll();
    }

    //----------------- READ BY SIFRA -----------------
    @GetMapping("/findBySifra/{sifra}")
    public VisokoSkolskaUstanovaResponse findBySifra(@PathVariable String sifra) {
        return service.findBySifra(sifra);
    }

    //----------------- UPDATE -----------------
    @PostMapping("/update/{sifra}")
    public VisokoSkolskaUstanovaResponse update(
            @PathVariable String sifra,
            @RequestBody VisokoSkolskaUstanovaRequest noviPodaci) {
        return service.update(sifra, noviPodaci);
    }

    //----------------- DELETE -----------------
    @GetMapping("/delete/{sifra}")
    public void delete(@PathVariable String sifra) {
        service.delete(sifra);
    }
}
