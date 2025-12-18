package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.services.GrupaService;
import org.springframework.web.bind.annotation.*;
import request.GrupaRequest;
import response.GrupaResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/grupa")
@RequiredArgsConstructor
public class GrupaController {
    //PRIJAVA ISPITA
    //OBNOVA PREDMETI KOJI SMEJU UOPSTE DA SE OBNOVE/UPISU
    private final GrupaService grupaService;

    @PostMapping("/save")
    public GrupaResponse save(@RequestBody @Valid GrupaRequest request) {
        return grupaService.save(request);
    }

    @GetMapping("/findAll")
    public List<GrupaResponse> findAll() {
        return grupaService.findAll();
    }

    @GetMapping("/findById/{id}")
    public GrupaResponse findById(@PathVariable Long id) {
        return grupaService.findById(id);
    }

    @PostMapping("/update/{id}")
    public GrupaResponse update(@PathVariable Long id, @RequestBody @Valid GrupaRequest request) {
        return grupaService.update(id, request);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        grupaService.delete(id);
    }
}
