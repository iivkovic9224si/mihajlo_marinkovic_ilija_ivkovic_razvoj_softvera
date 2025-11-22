package org.raflab.studsluzba.controllers.logicControllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.services.SlusaPredmetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/slusapredmet")
@AllArgsConstructor
public class SlusaPredmetController {

    private final SlusaPredmetService slusaPredmetService;

    @PostMapping("/dodaj")
    public SlusaPredmetResponse save(@RequestBody SlusaPredmetRequest request) {
        return slusaPredmetService.save(request);
    }


    @PutMapping(path = "/update/{id}")
    public void update(@PathVariable Long id, @RequestBody SlusaPredmetRequest request) {
        request.setId(id);
        slusaPredmetService.update(request);
    }

    @DeleteMapping(path = "/obrisi/{id}")
    public void delete(@PathVariable Long id) {
        SlusaPredmetRequest request = new SlusaPredmetRequest(id, null, null, 0, 0, null, null);
        slusaPredmetService.delete(request);
    }

    @GetMapping(path = "/{id}")
    public Optional<SlusaPredmetResponse> findById(@PathVariable Long id) {
        return slusaPredmetService.findById(id);
    }

    @GetMapping(path = "/student")
    public List<SlusaPredmetResponse> getSlusaPredmetiZaStudenta(
            @RequestParam String studProgramOznaka,
            @RequestParam int studentGodina,
            @RequestParam int studentBroj
    ) {
        return slusaPredmetService.getSlusaPredmetiZaStudenta(studProgramOznaka, studentGodina, studentBroj);
    }

    @GetMapping(path = "/sve")
    public List<SlusaPredmetResponse> findAll() {
        return slusaPredmetService.findAll();
    }

}
