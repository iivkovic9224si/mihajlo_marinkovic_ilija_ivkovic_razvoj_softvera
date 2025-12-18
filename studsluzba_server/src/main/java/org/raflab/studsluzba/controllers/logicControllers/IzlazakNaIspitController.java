package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.services.IzlazakNaIspitService;
import org.springframework.web.bind.annotation.*;
import request.IzlazakNaIspitRequest;
import response.IzlazakNaIspitResponse;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/izlazakNaIspit")
@RequiredArgsConstructor
public class IzlazakNaIspitController {

    private final IzlazakNaIspitService izlazakNaIspitService;

    @PostMapping("/save")
    public IzlazakNaIspitResponse save(@RequestBody IzlazakNaIspitRequest request) {
        return izlazakNaIspitService.save(request);
    }

    @PostMapping("/saveWithPolozen")
    public IzlazakNaIspitResponse saveWithPolozen(@RequestBody IzlazakNaIspitRequest request) {
        return izlazakNaIspitService.saveWithPolozenPredmetIfPass(request);
    }

    @GetMapping("/findAll")
    public List<IzlazakNaIspitResponse> findAll() { return izlazakNaIspitService.findAll(); }

    @GetMapping("/findById/{id}")
    public IzlazakNaIspitResponse findById(@PathVariable Long id) { return izlazakNaIspitService.findById(id); }

    @PostMapping("/update/{id}")
    public IzlazakNaIspitResponse update(@PathVariable Long id, @RequestBody IzlazakNaIspitRequest request) {
        return izlazakNaIspitService.update(id, request);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) { izlazakNaIspitService.delete(id); }

    @GetMapping("/prijava/{prijavaId}")
    public List<IzlazakNaIspitResponse> findByPrijava(@PathVariable Long prijavaId) {
        return izlazakNaIspitService.findByPrijavaIspitaId(prijavaId);
    }

    @GetMapping("/izlasci/{studentId}/{drziPredmetId}")
    public Long countTimes(@PathVariable Long studentId, @PathVariable Long drziPredmetId) {
        return izlazakNaIspitService.countStudentPolagaoPredmet(studentId, drziPredmetId);
    }

}

