package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.PrijavaIspitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import request.PrijavaIspitaRequest;
import response.PrijavaIspitaResponse;
import response.StudentIndeksResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/prijavaIspita")
public class PrijavaIspitaController {

    @Autowired
    private PrijavaIspitaService prijavaIspitaService;

    // ------------------ CREATE / SAVE ------------------
    @PostMapping("/save")
    public PrijavaIspitaResponse save(@RequestBody @Valid PrijavaIspitaRequest request) {
        return prijavaIspitaService.save(request);
    }

    // ------------------ FIND ALL ------------------
    @GetMapping("/findAll")
    public List<PrijavaIspitaResponse> findAll() {
        return prijavaIspitaService.findAll();
    }

    // ------------------ FIND BY ID ------------------
    @GetMapping("/findById/{id}")
    public PrijavaIspitaResponse findById(@PathVariable Long id) {
        return prijavaIspitaService.findById(id);
    }

    // ------------------ DELETE ------------------
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        prijavaIspitaService.delete(id);
    }

    @GetMapping("/studentiZaIspit/{ispitId}")
    public List<StudentIndeksResponse> getStudentiZaIspit(@PathVariable Long ispitId) {
        return prijavaIspitaService.findStudentiByIspitId(ispitId);
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/update/{id}")
    public PrijavaIspitaResponse update(
            @PathVariable Long id,
            @RequestBody @Valid PrijavaIspitaRequest request) {
        return prijavaIspitaService.update(id, request);
    }

    @GetMapping("/prijaveZaStudenta/{godina}/{broj}/{oznaka}")
    public List<PrijavaIspitaResponse> getPrijaveZaStudenta(
            @PathVariable Integer godina,
            @PathVariable Integer broj,
            @PathVariable String oznaka) {
        return prijavaIspitaService.findPrijaveZaStudenta(godina, broj, oznaka);
    }

    // ✅ NOVO: Dostupni ispiti za studenta
    @GetMapping("/dostupniIspiti/{godina}/{broj}/{oznaka}")
    public List<PrijavaIspitaResponse> getDostupniIspitiZaStudenta(
            @PathVariable Integer godina,
            @PathVariable Integer broj,
            @PathVariable String oznaka) {
        return prijavaIspitaService.findDostupniIspitiZaStudenta(godina, broj, oznaka);
    }

    @GetMapping("/aktivniDostupniIspiti/{godina}/{broj}/{oznaka}")
    public List<PrijavaIspitaResponse> getAktivniDostupniIspitiZaStudenta(
            @PathVariable Integer godina,
            @PathVariable Integer broj,
            @PathVariable String oznaka) {
        return prijavaIspitaService.findAktivniDostupniIspitiZaStudenta(godina, broj, oznaka);
    }

}
