package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import request.SkolskaGodinaRequest;
import response.SkolskaGodinaResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/skolskaGodina")
public class SkolskaGodinaController {

    @Autowired
    private SkolskaGodinaService skolskaGodinaService;

    @PostMapping("/save")
    public SkolskaGodinaResponse save(@RequestBody @Valid SkolskaGodinaRequest skolskaGodinaRequest) throws Exception {
        return skolskaGodinaService.save(skolskaGodinaRequest);
    }

    @GetMapping("/findAll")
    public List<SkolskaGodinaResponse> findAll() {
        return skolskaGodinaService.findAll();
    }

    @GetMapping("/findById/{id}")
    public SkolskaGodinaResponse findById(@PathVariable Long id) {
        return skolskaGodinaService.findById(id);
    }

    @PostMapping("/update")
    public SkolskaGodinaResponse updateAktivna(@RequestBody @Valid SkolskaGodinaRequest skolskaGodinaRequest) {
        return skolskaGodinaService.update(skolskaGodinaRequest);
    }



    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        skolskaGodinaService.delete(id);
    }

    @GetMapping("/aktivna")
    public SkolskaGodinaResponse getActiveYear() {
        return skolskaGodinaService.findActiveYear();
    }
}
