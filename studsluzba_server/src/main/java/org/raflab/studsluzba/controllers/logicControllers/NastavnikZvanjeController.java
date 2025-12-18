package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.services.NastavnikZvanjeService;
import org.springframework.web.bind.annotation.*;
import request.NastavnikZvanjeRequest;
import response.NastavnikZvanjeResponse;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/nastavnikZvanje")
@RequiredArgsConstructor
public class NastavnikZvanjeController {
    private final NastavnikZvanjeService service;

    @PostMapping("/save")
    public NastavnikZvanjeResponse save(@RequestBody @Valid NastavnikZvanjeRequest request) {
        return service.save(request);
    }

    @GetMapping("/findAll")
    public List<NastavnikZvanjeResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/findById/{id}")
    public NastavnikZvanjeResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/update")
    public NastavnikZvanjeResponse update(@RequestBody @Valid NastavnikZvanjeRequest request) {
        return service.update(request);
    }

    // DELETE preko GET-a kao kod nastavnika
    @GetMapping("/delete")
    public void delete(@RequestParam(required = false) Long id,
                       @RequestParam(required = false) Long nastavnikId,
                       @RequestParam(required = false) String datumIzbora) {
        LocalDate date = datumIzbora != null ? LocalDate.parse(datumIzbora) : null;
        service.delete(id, nastavnikId, date);
    }
}
