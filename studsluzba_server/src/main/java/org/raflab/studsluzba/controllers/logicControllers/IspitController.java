package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.services.IspitService;
import org.springframework.web.bind.annotation.*;
import request.IspitRequest;
import response.IspitResponse;
import response.RezultatiIspitaResponse;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispit")
@RequiredArgsConstructor
public class IspitController {

    private final IspitService ispitService;

    @PostMapping("/save")
    public Optional<IspitResponse> save(@RequestBody IspitRequest request) {
        return ispitService.save(request);
    }

    @GetMapping("/findAll")
    public List<IspitResponse> findAll() {
        return ispitService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Optional<IspitResponse> findById(@PathVariable Long id) {
        return ispitService.findById(id);
    }

    @PostMapping("/update")
    public Optional<IspitResponse> update(@RequestBody IspitRequest request) {
        return ispitService.update(request);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ispitService.delete(id);
    }

    @GetMapping(path = "/rezultati/{id}")
    public List<RezultatiIspitaResponse> rezultati(@PathVariable Long id) {
        return ispitService.getRezultatiSaIspita(id);
    }

    //PROSECNA OCENA ISPITA
    @GetMapping(path = "/prosecnaOcena/{id}")
    public Double prosecnaOcena(@PathVariable Long id) {
        return ispitService.findProsecnaOcenaNaIspitu(id);
    }

}
