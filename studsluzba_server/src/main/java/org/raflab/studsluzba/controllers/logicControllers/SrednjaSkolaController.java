package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.entity.VrstaSrednjeSkole;
import org.raflab.studsluzba.services.SrednjaSkolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/findById/{id}")
    public Optional<SrednjaSkolaResponse> findById(@PathVariable Long id) {
        return Optional.ofNullable(srednjaSkolaService.findById(id));
    }
    @GetMapping("/findByNazivAndVrsta")
    public SrednjaSkolaResponse findByNazivAndVrsta(
            @RequestParam String naziv,
            @RequestParam VrstaSrednjeSkole vrsta
    ) {
        return srednjaSkolaService.findByNazivAndVrsta(naziv, vrsta);
    }
    @PostMapping("/update")
    public SrednjaSkolaResponse update(
            @RequestParam String stariNaziv,
            @RequestParam VrstaSrednjeSkole staraVrsta,
            @RequestBody SrednjaSkolaRequest noviPodaci)
    {
        return srednjaSkolaService.update(stariNaziv, staraVrsta, noviPodaci);
    }
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        srednjaSkolaService.delete(id);
    }
}
