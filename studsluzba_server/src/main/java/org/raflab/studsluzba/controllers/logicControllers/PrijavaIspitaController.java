package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.services.PrijavaIspitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
