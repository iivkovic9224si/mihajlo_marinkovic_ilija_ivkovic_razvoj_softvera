package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.model.dtos.RezultatiIspitaDTO;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.services.IspitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispit")
public class IspitController {
    @Autowired
    IspitService ispitService;

    @GetMapping(path = "/rezultati/{id}")
    public List<RezultatiIspitaDTO> rezultati(@PathVariable Long id) {
        Optional<Ispit> ispit = ispitService.findById(id);

        if(ispit.isEmpty()) {
            return null;
        }
        return ispit.map(ispitService::getRezultatiSaIspita).orElse(null);
    }

    @GetMapping(path = "/prosecnaOcena/{id}")
    public Double prosecnaOcena(@PathVariable Long id) {
        Optional<Ispit> ispit = ispitService.findById(id);

        return ispit.map(ispitService::findProsecnaOcenaNaIspitu).orElse(null);
    }
}
