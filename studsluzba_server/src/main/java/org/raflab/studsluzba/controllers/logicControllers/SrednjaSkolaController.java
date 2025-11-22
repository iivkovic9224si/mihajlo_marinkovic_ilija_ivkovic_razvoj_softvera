package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.services.SrednjaSkolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/srednjaSkola")
public class SrednjaSkolaController {

    @Autowired
    private SrednjaSkolaService srednjaSkolaService;

    @PostMapping("/save")
    public SrednjaSkolaResponse save(@RequestBody SrednjaSkolaRequest request) {
        return srednjaSkolaService.save(request);
    }

    @GetMapping("/all")
    public List<SrednjaSkolaResponse> findAll() {
        return srednjaSkolaService.findAll();
    }

    @GetMapping("/findBySifra/{sifra}")
    public SrednjaSkolaResponse findBySifra(@PathVariable String sifra) {
        return srednjaSkolaService.findBySifra(sifra);
    }

    @PostMapping("/update/{sifra}")
    public SrednjaSkolaResponse update(
            @PathVariable String sifra,
            @RequestBody SrednjaSkolaRequest noviPodaci
    ) {
        return srednjaSkolaService.update(sifra, noviPodaci);
    }

    @GetMapping("/delete/{sifra}")
    public void delete(@PathVariable String sifra) {
        srednjaSkolaService.delete(sifra);
    }
}
